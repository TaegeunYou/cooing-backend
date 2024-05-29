package com.alpha.kooing.house.dto

import com.alpha.kooing.reward.enum.RewardType

class House(
    val items: List<HouseItem> = emptyList()
) {
    class HouseItem(
        val name: String? = null,
        val x: String? = null,
        val y: String? = null,
    )
}