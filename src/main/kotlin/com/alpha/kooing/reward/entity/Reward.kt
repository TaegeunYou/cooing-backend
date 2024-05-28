package com.alpha.kooing.reward.entity

import com.alpha.kooing.reward.enum.RewardType
import jakarta.persistence.*

@Entity
class Reward(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    val rewardType: RewardType,

    @Column(nullable = false)
    val count: Int
)