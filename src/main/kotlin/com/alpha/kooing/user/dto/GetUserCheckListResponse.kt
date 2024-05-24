package com.alpha.kooing.user.dto

class GetUserCheckListResponse(
    val income: List<String>, //   소득
    val housing: List<String>, //  주거
    val finance: List<String>, //  금융
    val education: List<String>, //    진학
    val employment: List<String>, //   취업
    val health: List<String>, //   의료
    val miscellaneous: List<String>, //    기타
    val tips: List<String>, // 꿀팁
)