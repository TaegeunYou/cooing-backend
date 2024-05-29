package com.alpha.kooing.reward.event

import com.alpha.kooing.reward.enum.RewardRequirementType
import com.alpha.kooing.user.User

class ProvideRewardEvent(
    val user: User,
    val rewardRequirementType: RewardRequirementType
)