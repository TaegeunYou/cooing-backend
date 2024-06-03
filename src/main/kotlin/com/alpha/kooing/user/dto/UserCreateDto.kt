package com.alpha.kooing.user.dto

import com.alpha.kooing.user.enum.RoleType
import org.springframework.web.multipart.MultipartFile

class UserCreateDto(
    val nickname: String,
    val role: RoleType,
    val profileMessage: String,
    val interestKeyword: List<Int>,
    val concernKeyword: List<Int>,
    val isMatchingActive: Boolean,
) {
    class RewardDetail(
        val name: String,
        val count: Int
    )
}