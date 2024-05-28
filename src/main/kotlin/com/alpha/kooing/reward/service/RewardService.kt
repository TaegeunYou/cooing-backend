package com.alpha.kooing.reward.service

import com.alpha.kooing.reward.entity.Reward
import com.alpha.kooing.reward.entity.UserReward
import com.alpha.kooing.reward.enum.RewardRequirementType
import com.alpha.kooing.reward.event.ProvideRewardEvent
import com.alpha.kooing.reward.repository.RewardRepository
import com.alpha.kooing.reward.repository.UserRewardRepository
import com.alpha.kooing.user.User
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RewardService(
    private val userRewardRepository: UserRewardRepository,
    private val rewardRepository: RewardRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun createUserReward(user: User, rewardRequirementType: RewardRequirementType) {
        val newReward = this.getRandomRewardFromRemaining(user)
        if (newReward != null) {
            userRewardRepository.save(
                UserReward(
                    null,
                    user,
                    newReward,
                    rewardRequirementType
                )
            )
            applicationEventPublisher.publishEvent(ProvideRewardEvent(user, rewardRequirementType))
        }
    }

    //전체 가질 수 있는 리워드에서 유저가 갖고 있는 리워드를 제외한 것들 중 랜덤으로 한개 리턴
    private fun getRandomRewardFromRemaining(user: User): Reward? {
        val rewardList = rewardRepository.findAll().flatMap { reward ->
            List(reward.count) {
                reward
            }
        }.toMutableList()
        user.userRewards.forEach { userReward ->
            val index = rewardList.indexOfFirst { reward ->
                reward.id == userReward.id
            }
            if (index != -1) {
                rewardList.removeAt(index)
            }
        }
        return if (rewardList.isNotEmpty()) {
            rewardList.random()
        } else {
            null
        }
    }

}