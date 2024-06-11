package com.alpha.kooing.house.dto

class GetHouseResponse(
    val house: House,
    val rewardList: List<Int>,
    val isMatchingActive: Boolean,
)