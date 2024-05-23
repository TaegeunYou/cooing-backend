package com.alpha.kooing.support.enum

/*
사업신청기간반복구분코드
1. 상시(002001)
2. 연간반복(002002)
3. 월간반복(002003)
4. 특정기간(002004)
5. 미정(002005)
 */
enum class SupportPolicyPeriodType(val code: String) {
    상시("002001"),
    연간반복("002002"),
    월간반복("002003"),
    특정기간("002004"),
    미정("002005")
}