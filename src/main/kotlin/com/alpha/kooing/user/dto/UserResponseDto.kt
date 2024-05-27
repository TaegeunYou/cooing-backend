package com.alpha.kooing.user.dto

import com.alpha.kooing.user.Role
import com.alpha.kooing.user.entity.UserConcernKeyword
import com.alpha.kooing.user.entity.UserInterestKeyword

class UserResponseDto(
    val id:Long?,
    val username:String,
    val email:String,
    val role: Role,
    val profileMessage: String,
    val profileImageUrl:String?,
    val userInterestKeyword: List<String>,
    val userConcernKeyword: List<String>
){
    override fun toString(): String {
        return "UserResponseDto(username='$username', email='$email', role=$role)"
    }
}