package com.alpha.kooing.user.dto

import com.alpha.kooing.user.enum.RoleType
import org.springframework.web.multipart.MultipartFile

class UserCreateDto(
    val role: RoleType,
    val profileMessage: String,
    val interestKeyword: List<String>,
    val concernKeyword: List<String>,
    val isMatchingActive: Boolean,
) {
    class RewardDetail(
        val name: String,
        val count: Int
    )
}