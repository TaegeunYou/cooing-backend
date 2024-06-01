package com.alpha.kooing.user.service

import com.alpha.kooing.config.LoginUserManager
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.external.AmazonS3Service
import com.alpha.kooing.reward.enum.RewardType
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.*
import com.alpha.kooing.user.User
import com.alpha.kooing.user.entity.MatchUser
import com.alpha.kooing.user.entity.UserConcernKeyword
import com.alpha.kooing.user.entity.UserInterestKeyword
import com.alpha.kooing.user.enum.RoleType
import com.alpha.kooing.user.event.SignUpEvent
import com.alpha.kooing.user.repository.*
import com.alpha.kooing.userMatching.repository.UserMatchingRepository
import com.alpha.kooing.util.ImageUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val amazonS3Service: AmazonS3Service,
    private val userRepository: UserRepository,
    private val userInterestKeywordRepository: UserInterestKeywordRepository,
    private val userConcernKeywordRepository: UserConcernKeywordRepository,
    private val interestKeywordRepository: InterestKeywordRepository,
    private val concernKeywordRepository: ConcernKeywordRepository,
    private val userMatchingRepository: UserMatchingRepository,
    private val userManager: LoginUserManager,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val matchUserRepository: MatchUserRepository,
    private val awsS3Service: AmazonS3Service,

) {

    @Transactional(readOnly = true)
    fun getUser(token: String): UserDetail {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        val userRewards = user.userRewards.groupBy {
            it.reward.rewardType
        }.map {
            Pair(it.key, it.value.size)
        }
        val userInterestKeyword = user.userInterestKeyword.map { it.interestKeyword }
        val userConcernKeyword = user.userConcernKeyword.map { it.concernKeyword }
        val interestKeywordAll = interestKeywordRepository.findAll()
        val concernKeywordAll = concernKeywordRepository.findAll()
        return UserDetail(
            user.username,
            user.roleType,
            user.profileMessage,
            user.profileImageUrl,
            interestKeywordAll.map { interestKeyword ->
                if (interestKeyword in userInterestKeyword) 1 else 0
            },
            concernKeywordAll.map { concernKeyword ->
                if (concernKeyword in userConcernKeyword) 1 else 0
            },
            RewardType.entries.map { rewardType ->
                UserDetail.RewardDetail(
                    rewardType.name,
                    userRewards.firstOrNull {
                        it.first == rewardType
                    }?.second ?: 0
                )
            },
            user.isMatchingActive
        )
    }

    @Transactional
    fun saveUser(token:String, userInfo:UserCreateDto, profileImage: MultipartFile?):User?{
        val existUser = userRepository.findByEmail(jwtTokenProvider.getJwtEmail(token)).orElse(null)
        if(existUser!=null) return null
        val imageUrl = if(profileImage!=null) awsS3Service.upload(profileImage, "profile") else null
        val user = userRepository.save(
            User(
                email = jwtTokenProvider.getJwtEmail(token),
                username = jwtTokenProvider.getJwtUsername(token),
                role = Role.USER,
                isMatchingActive = false,
                profileImageUrl = imageUrl,
                profileMessage = userInfo.profileMessage,
                roleType = userInfo.role,
            )
        )
        println(concernKeywordRepository.findAllByName(userInfo.concernKeyword[0]).getOrNull()?:"no such keyword")
        userInfo.concernKeyword.forEach {
            val concernKeyword = concernKeywordRepository.findAllByName(it).getOrNull()?:return null
            userConcernKeywordRepository.save(UserConcernKeyword(user=user, concernKeyword = concernKeyword))
        }
        userInfo.interestKeyword.forEach {
            val interestKeyword = interestKeywordRepository.findAllByName(it).getOrNull()?:return null
            userInterestKeywordRepository.save(UserInterestKeyword(user=user, interestKeyword = interestKeyword))
        }
        applicationEventPublisher.publishEvent(SignUpEvent(user))
        return user
    }


    @Transactional
    fun updateUserProfile(token: String, request: UpdateUserProfileRequest, profileImage: MultipartFile?) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        user.updateProfile(request.name, request.profileMessage)
        if (profileImage != null) {
            val profileImageUrl = amazonS3Service.upload(profileImage, "profile")
            user.updateProfileImage(profileImageUrl)
        }
        userRepository.save(user)
    }

    @Transactional
    fun updateUserMatchingStatus(token: String, request: UpdateUserMatchingStatusRequest) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        // 매칭 취소 시 매칭 정보 제거
        if (!request.isMatchingActive && user.id!=null) matchUserRepository.deleteAllByUserId(user.id)
        // 정보 갱신
        user.updateMatchingStatus(request.isMatchingActive)
        userRepository.save(user)
    }

    @Transactional
    fun updateUserMatchingKeyword(token: String, request: UpdateUserMatchingKeywordRequest) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        val interestKeywords = interestKeywordRepository.findAll()
        val concernKeywords = concernKeywordRepository.findAll()
        userInterestKeywordRepository.deleteAllById(user.userInterestKeyword.map { it.id })
        userConcernKeywordRepository.deleteAllById(user.userConcernKeyword.map { it.id })
        userInterestKeywordRepository.saveAll(
            interestKeywords
                .filterIndexed { idx ,it ->
                    request.interestKeyword[idx] == 1
                }
                .map { updateInterestKeyword ->
                    UserInterestKeyword(null, user, updateInterestKeyword)
                }
        )
        concernKeywords
            .filterIndexed { idx ,it ->
                request.concernKeyword[idx] == 1
            }
            .map { updateConcernKeyword ->
                UserConcernKeyword(null, user, updateConcernKeyword)
            }
        userConcernKeywordRepository.saveAll(
            concernKeywords
                .filterIndexed { idx ,it ->
                    request.concernKeyword[idx] == 1
                }
                .map { updateConcernKeyword ->
                    UserConcernKeyword(null, user, updateConcernKeyword)
                }
        )
        this.updateUserMatchingKeywordEvent(user)
    }

    @Transactional
    fun findAll(): List<UserResponseDto>?{
        val result = userRepository.findAll()
        val interestKeywordAll = interestKeywordRepository.findAll()
        val concernKeywordAll = concernKeywordRepository.findAll()
        return result.map { it.toResponseDto(interestKeywordAll, concernKeywordAll) }
    }
    /**
     * 1. income 소득 30-40
     * 2. housing 주거 44-77
     * 3. finance 금융 80-94
     * 4. education 진학 98-107
     * 5. employment 취업 110-132
     * 6. health 의료 및 건강 136-159
     * 7. miscellaneous 기타 162-170
     * 8. tips 알아두면 꿀 팁 174-185
     */
    @Transactional(readOnly = true)
    fun getUserCheckList(): GetUserCheckListResponse {
        val incomeList = getImageUrlList(30, 40)
        val housingList = getImageUrlList(44, 77)
        val financeList = getImageUrlList(80, 94)
        val educationList = getImageUrlList(98, 107)
        val employmentList = getImageUrlList(110, 132)
        val healthList = getImageUrlList(136, 159)
        val miscellaneousList = getImageUrlList(162, 170)
        val tipsList = getImageUrlList(174, 185)
        return GetUserCheckListResponse(
            incomeList,
            housingList,
            financeList,
            educationList,
            employmentList,
            healthList,
            miscellaneousList,
            tipsList
        )
    }

    private fun getImageUrlList(startPage: Int, endPage: Int): List<String> {
        return (startPage..endPage).map { page ->
            "https://cooing-bucket.s3.ap-northeast-2.amazonaws.com/checklist/2024%E1%84%82%E1%85%A7%E1%86%AB+%E1%84%8C%E1%85%A1%E1%84%85%E1%85%B5%E1%86%B8%E1%84%8C%E1%85%A5%E1%86%BC%E1%84%87%E1%85%A9%E1%84%87%E1%85%AE%E1%86%A8+%E1%84%8E%E1%85%AC%E1%84%8C%E1%85%A9%E1%86%BC_page-${String.format("%04d", page)}.jpg"
        }
    }


    @Transactional
    fun save(user: User): UserResponseDto? {
        val interestKeywordAll = interestKeywordRepository.findAll()
        val concernKeywordAll = concernKeywordRepository.findAll()
        try {
            if(userRepository.findByEmail(user.email).orElse(null)!=null){
                return null
            }
            val result = userRepository.save(user)
            println("user : ${user.toString()}")
            val resDto = result.toResponseDto(interestKeywordAll, concernKeywordAll)
            println("resDto : ${resDto.toString()}")
            return resDto
        }catch (e: Exception){
            e.printStackTrace()
            return null
        }
    }

    @Transactional
    fun findMatchingUser(ikw:List<String>, ckw:List<String>, userId:Long): List<User>{
        val users = userManager.getLoginUserList()
        val matchUser = users.map { it ->
            val user = userRepository.findByEmail(it.email).orElse(null)
            if(user == null || user.id == userId){
                null
            }else{
                val checkCkw = user.userConcernKeyword.any{ it.concernKeyword.name in ckw }
                val checkIkw = user.userInterestKeyword.any { it.interestKeyword.name in ikw }
                if(checkIkw && checkCkw) user else null
            }
        }.filterNotNull()
        return matchUser
    }

    @Transactional
    fun findOrCreateMatchUser(userId:Long):List<UserResponseDto>?{
        val currentUser = userRepository.findById(userId).orElse(null)?:return null
        var matchUsers = matchUserRepository.findAllByUserId(userId)
        val interestKeywordAll = interestKeywordRepository.findAll()
        val concernKeywordAll = concernKeywordRepository.findAll()
        if(matchUsers.isEmpty()){
            val ckw = currentUser.userConcernKeyword.map { it.concernKeyword.name }
            val ikw = currentUser.userInterestKeyword.map { it.interestKeyword.name }
                val keywordMatchUsers = findMatchingUser(ikw, ckw, userId)
            keywordMatchUsers.forEach{ matchUserRepository.save(MatchUser(user = currentUser, matchUser = it)) }
            return keywordMatchUsers.map { it.toResponseDto(interestKeywordAll, concernKeywordAll) }
        }
        return matchUsers.map { it.user.toResponseDto(interestKeywordAll, concernKeywordAll) }
    }

    private fun updateUserMatchingKeywordEvent(user: User) {
//        applicationEventPublisher.publishEvent(ChangeUserKeywordEvent(user, user.ma))
    }
}