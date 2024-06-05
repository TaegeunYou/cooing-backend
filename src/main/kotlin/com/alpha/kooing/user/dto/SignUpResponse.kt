package com.alpha.kooing.user.dto

class SignUpResponse(
    val userId: Long,
    val token: String,
    val isMatchingSuccess: Boolean
)