package com.alpha.kooing.reward.repository

import com.alpha.kooing.reward.entity.Reward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RewardRepository: JpaRepository<Reward, Long>