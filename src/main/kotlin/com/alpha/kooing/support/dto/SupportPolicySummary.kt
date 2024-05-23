package com.alpha.kooing.support.dto

import com.alpha.kooing.support.entity.SupportPolicy
import jakarta.persistence.Column

class SupportPolicySummary(
    val id: Long,
    val polyBizSjnm: String? = null, //	정책명
    val polyItcnCn: String? = null, //	정책소개
)