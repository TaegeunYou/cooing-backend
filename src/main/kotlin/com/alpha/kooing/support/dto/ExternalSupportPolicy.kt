package com.alpha.kooing.support.dto

data class ExternalSupportPolicy(
    val pageIndex: Int,
    val totalCnt: Int,
    val youthPolicy: YouthPolicy
) {
    data class YouthPolicy(
        val rnum: Int
    )
}