package com.alpha.kooing.reward.repository

import com.alpha.kooing.notification.entity.Notification
import com.alpha.kooing.reward.entity.UserReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRewardRepository: JpaRepository<UserReward, Long> {
    fun findAllByUserId(userId: Long): List<UserReward>
}