package com.alpha.kooing.config

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.UserResponseDto
import org.springframework.stereotype.Component

@Component
class LoginUserManager(
    val jwtTokenProvider: JwtTokenProvider
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
        val userList = users.map { user ->
            val token = user.value
            if(jwtTokenProvider.isExpired(token)){
                users.remove(user.key)
                null
            }else{
                println(token)
                val userDto = UserResponseDto(
                    id = user.key,
                    username = jwtTokenProvider.getJwtUsername(token),
                    email = jwtTokenProvider.getJwtEmail(token),
                    role = Role.valueOf(jwtTokenProvider.getJwtRole(token))
                )
                userDto
            }
        }.filterNotNull()
        return userList
    }
}