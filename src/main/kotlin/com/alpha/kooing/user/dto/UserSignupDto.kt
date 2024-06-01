package com.alpha.kooing.user.dto

import com.alpha.kooing.user.enum.RoleType
import java.awt.Image

class UserSignupDto(
    val name: String,
    val role: RoleType,
    val profileMessage: String,
    val profileImage: Image?,
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