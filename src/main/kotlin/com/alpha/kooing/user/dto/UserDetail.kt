package com.alpha.kooing.user.dto

import com.alpha.kooing.user.enum.RoleType

class UserDetail(
    val name: String,
    val role: RoleType,
    val profileMessage: String,
    val profileImageUrl: String?,
    val interestKeywordType: List<String>,
    val concernKeywordType: List<String>,
)