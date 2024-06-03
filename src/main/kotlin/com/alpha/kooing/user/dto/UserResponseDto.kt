package com.alpha.kooing.user.dto

import com.alpha.kooing.user.Role
import com.alpha.kooing.user.enum.RoleType

class UserResponseDto(
    val id:Long?,
    val username:String,
    val email:String,
    val role: Role,
    val roleType: RoleType,
    val profileMessage: String,
    val profileImageUrl:String?,
    val userInterestKeyword: List<Int>,
    val userConcernKeyword: List<Int>,
    val isMatchingActive: Boolean,
){
    override fun toString(): String {
        return "UserResponseDto(username='$username', email='$email', role=$role)"
    }
}