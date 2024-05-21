package com.alpha.kooing.support.enum

/*
1. 일자리 분야(023010)
2. 주거 분야(023020)
3. 교육 분야(023030)
4. 복지.문화 분야(023040)
5. 참여.권리 분야(023050)
 */
enum class SupportPolicyType(
    val value: String,
    val code: String
) {
    일자리("일자리", "023010"),
    주거("주거", "023020"),
    교육("교육", "023030"),
    복지_문화("복지.문화", "023040"),
    참여_권리("참여.권리", "023050"),
}