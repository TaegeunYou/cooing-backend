package com.alpha.kooing.user.dto

import com.alpha.kooing.user.enum.RoleType

class UserDetail(
    val name: String,
    val role: RoleType,
    val profileMessage: String,
    val profileImageUrl: String?,
    val interestKeyword: List<String>,
    val concernKeyword: List<String>,
    val rewards: List<RewardDetail>,
    val isMatchingActive: Boolean,
) {
    class RewardDetail(
        val name: String,
        val count: Int
    )
}