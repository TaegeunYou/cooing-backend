package com.alpha.kooing.user.dto

import com.alpha.kooing.user.Role

class UserResponseDto(
    val id:Long?,
    val username:String,
    val email:String,
    val role: Role
)