package com.alpha.kooing.user.service

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.external.AmazonS3Service
import com.alpha.kooing.user.dto.UpdateUserMatchingKeywordRequest
import com.alpha.kooing.user.dto.UpdateUserMatchingStatusRequest
import com.alpha.kooing.user.dto.UpdateUserProfileRequest
import com.alpha.kooing.user.dto.UserDetail
import com.alpha.kooing.user.entity.UserConcernKeyword
import com.alpha.kooing.user.entity.UserInterestKeyword
import com.alpha.kooing.user.repository.*
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
) {

    @Transactional(readOnly = true)
    fun getUser(token: String): UserDetail {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        return UserDetail(
            user.username,
            user.roleType,
            user.profileMessage,
            user.profileImageUrl,
            user.userInterestKeyword.map {
                it.interestKeyword.name
            },
            user.userConcernKeyword.map {
                it.concernKeyword.name
            }
        )
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
                .filter { it.name in request.interestKeyword }
                .map { updateInterestKeyword ->
                    UserInterestKeyword(null, user, updateInterestKeyword)
                }
        )
        userConcernKeywordRepository.saveAll(
            concernKeywords
                .filter { it.name in request.concernKeyword }
                .map { updateConcernKeyword ->
                    UserConcernKeyword(null, user, updateConcernKeyword)
                }
        )
    }


}