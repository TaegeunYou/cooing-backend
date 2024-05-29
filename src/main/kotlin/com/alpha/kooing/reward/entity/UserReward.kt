package com.alpha.kooing.reward.entity

import com.alpha.kooing.reward.enum.RewardRequirementType
import com.alpha.kooing.user.User
import jakarta.persistence.*

@Entity
class UserReward(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "reward_id")
    val reward: Reward,

    @Column(nullable = false)
    val rewardRequirementType: RewardRequirementType
)