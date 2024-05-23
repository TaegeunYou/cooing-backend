package com.alpha.kooing.user.dto

class UserCreateDto(
    val name:String,
    val role: String,
    val profileMessage:String,
    val profileImageUrl: String,
    val interestKeyword: Array<String>,
    val concernKeyword: Array<String>
)