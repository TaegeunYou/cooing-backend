package com.alpha.kooing.config

import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.entity.User
import org.apache.kafka.common.protocol.types.Field.Bool
import org.springframework.stereotype.Component

@Component
class LoginUserManager {
    companion object {
        private val users = mutableMapOf<Long, CustomOAuth2User>()
    }

    fun loginUser(userId:Long?, user:CustomOAuth2User){
        if(userId != null){
            users[userId] = user
        }
    }

    fun logoutUser(userId:Long?){
        users.remove(userId)
    }

    fun getLoginUserAll():MutableList<CustomOAuth2User>?{
        if(users.isNotEmpty()){
            val userList = users.values.toList() as MutableList<CustomOAuth2User>
            return userList
        }
        return null
    }
}