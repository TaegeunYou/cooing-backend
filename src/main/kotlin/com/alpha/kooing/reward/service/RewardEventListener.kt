package com.alpha.kooing.reward.service

import com.alpha.kooing.board.event.*
import com.alpha.kooing.chat.event.ChatWithMateOver1DayEvent
import com.alpha.kooing.chat.event.ChatWithMateOver3DayEvent
import com.alpha.kooing.chat.event.ChatWithMateOver5DayEvent
import com.alpha.kooing.reward.enum.RewardRequirementType
import com.alpha.kooing.chat.event.Chat100Event
import com.alpha.kooing.chat.event.Chat200Event
import com.alpha.kooing.chat.event.Chat300Event
import com.alpha.kooing.support.event.Scrap3JobPostingEvent
import com.alpha.kooing.support.event.Scrap3SupportBusinessEvent
import com.alpha.kooing.support.event.Scrap3SupportPolicyEvent
import com.alpha.kooing.user.User
import com.alpha.kooing.user.event.*
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener


@Component
class RewardEventListener(
    private val rewardService: RewardService,
) {

    @EventListener
    fun signUpEventHandler(event: SignUpEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.회원가입)
    }

    @EventListener
    fun attend3DaysEventHandler(event: Attend3DaysEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.출석체크_3일)
    }

    @EventListener
    fun attend7DaysEventHandler(event: Attend7DaysEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.출석체크_7일)
    }

    @EventListener
    fun attend2WeeksEventHandler(event: Attend2WeeksEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.출석체크_2주)
    }

    @EventListener
    fun attend1MonthEventHandler(event: Attend1MonthEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.출석체크_1달)
    }

    @EventListener
    fun chatWithMateOver1DayEventHandler(event: ChatWithMateOver1DayEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.메이트랑_1일_이상_채팅)
    }

    @EventListener
    fun chatWithMateOver3DaysEventHandler(event: ChatWithMateOver3DayEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.메이트랑_3일_이상_채팅)
    }

    @EventListener
    fun chatWithMateOver5DaysEventHandler(event: ChatWithMateOver5DayEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.메이트랑_5일_이상_채팅)
    }

    @EventListener
    fun comment1FreeBoardEventHandler(event: Comment1BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_댓글_1개_이상_작성)
    }

    @EventListener
    fun like1FreeBoardEventHandler(event: Like1BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_좋아요_1개_이상)
    }

    @EventListener
    fun scrapped1FreeBoardEventHandler(event: Scrap1BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_스크랩_1개_이상)
    }

    @EventListener
    fun posted1FreeBoardEventHandler(event: Post1BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_글_1개_이상_작성)
    }

    @EventListener
    fun comment5FreeBoardEventHandler(event: Comment5BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_댓글_5개_이상_작성)
    }

    @EventListener
    fun like5FreeBoardEventHandler(event: Like5BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_좋아요_5개_이상)
    }

    @EventListener
    fun scrap5FreeBoardEventHandler(event: Scrap5BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_스크랩_5개_이상)
    }

    @EventListener
    fun post3FreeBoardEventHandler(event: Post3BoardEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.자유게시판_글_3개_이상_작성)
    }

    @EventListener
    fun scrap3SupportPolicyEventHandler(event: Scrap3SupportPolicyEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.지원정책_스크랩_3개_이상)
    }

    @EventListener
    fun scrap3SupportBusinessEventHandler(event: Scrap3SupportBusinessEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.지원사업_스크랩_3개_이상)
    }

    @EventListener
    fun scrap3JobPostingEventHandler(event: Scrap3JobPostingEvent) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.채용공고_스크랩_3개_이상)
    }

    @EventListener
    fun chat100EventHandler(event: Chat100Event) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.채팅_100개_이상)
    }

    @EventListener
    fun chat200EventHandler(event: Chat200Event) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.채팅_200개_이상)
    }

    @EventListener
    fun chat300EventHandler(event: Chat300Event) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.채팅_300개_이상)
    }

    @EventListener
    fun postStudyVolunteerClub1EventHandler(event: PostStudyVolunteerClub1Event) {
        this.checkAvailAndCreateUserReward(event.user, RewardRequirementType.스터디_봉사활동_동아리_중_글_1개_이상_작성)
    }

    private fun checkAvailAndCreateUserReward(user: User, rewardRequirementType: RewardRequirementType) {
        if (this.isUserAlreadyReceived(user, rewardRequirementType)) {
            return
        }
        rewardService.createUserReward(user, rewardRequirementType)
    }

    private fun isUserAlreadyReceived(user: User, rewardRequirementType: RewardRequirementType): Boolean {
        return rewardRequirementType in user.userRewards.map {
            it.rewardRequirementType
        }
    }
}