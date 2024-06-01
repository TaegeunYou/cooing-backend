package com.alpha.kooing.user.dto

import com.alpha.kooing.user.enum.RoleType

class UserCreateDto(
    val role: RoleType,
    val profileMessage: String,
    val profileImageUrl: String?,
    val interestKeyword: List<String>,
    val concernKeyword: List<String>,
    val isMatchingActive: Boolean,
) {
    class RewardDetail(
        val name: String,
        val count: Int
    )
}