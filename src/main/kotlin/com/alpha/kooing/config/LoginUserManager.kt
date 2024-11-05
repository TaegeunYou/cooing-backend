package com.alpha.kooing.config

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.dto.UserResponseDto
import com.alpha.kooing.user.repository.ConcernKeywordRepository
import com.alpha.kooing.user.repository.InterestKeywordRepository
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class LoginUserManager(
    val jwtTokenProvider: JwtTokenProvider,
    val userRepository: UserRepository,
    val interestKeywordRepository: InterestKeywordRepository,
    val concernKeywordRepository: ConcernKeywordRepository
){
    companion object {
        private val users = mutableMapOf<Long, String>()
    }

    fun loginUser(userId:Long?, jwt:String){
        if(jwtTokenProvider.isExpired(token = jwt)) return
        if(userId != null){
            users[userId] = jwt
        }
    }

    fun logoutUser(userId:Long?){
        if(users.getOrDefault(userId, null)!=null){
            users.remove(userId)
        }
    }

    fun getLoginUserById(userId: Long?):String?{
        val token = users.getOrDefault(userId, null)?:return null
        if(jwtTokenProvider.isExpired(token)){
            users.remove(userId)
            return null
        }
        return token
    }

    fun getLoginUserList(): List<UserResponseDto> {
        val interestKeywordAll = interestKeywordRepository.findAll()
        val concernKeywordAll = concernKeywordRepository.findAll()
        val userList = users.map { user ->
            val token = user.value
            if(jwtTokenProvider.isExpired(token)){
                users.remove(user.key)
                null
            }else{
                val signInUser = userRepository.findByEmail(jwtTokenProvider.getJwtEmail(token)).orElse(null)
                signInUser?.toResponseDto(
                    interestKeywordAll,
                    concernKeywordAll
                )
            }
        }.filterNotNull()
        return userList
    }
}