package com.alpha.kooing.notification.service

import com.alpha.kooing.board.event.CreateBoardCommentEvent
import com.alpha.kooing.board.event.CreateBoardLikesEvent
import com.alpha.kooing.board.event.CreateBoardScrapEvent
import com.alpha.kooing.notification.enum.NotificationContentBoardType
import com.alpha.kooing.notification.enum.NotificationContentMateType
import com.alpha.kooing.notification.enum.NotificationTitleType
import com.alpha.kooing.reward.event.ProvideRewardEvent
import com.alpha.kooing.user.event.notification.ChangeUserKeywordEvent
import com.alpha.kooing.user.event.notification.MateMatchSuccessEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class NotificationEventListener(
    private val notificationService: NotificationService
) {

    @EventListener
    fun createBoardCommentEventHandler(event: CreateBoardCommentEvent) {
        notificationService.createBoardNotification(
            event.boardPostUser,
            NotificationTitleType.BOARD,
            NotificationContentBoardType.COMMENT,
            event.boardTitle
        )
    }

    @EventListener
    fun createBoardScrapEventHandler(event: CreateBoardScrapEvent) {
        notificationService.createBoardNotification(
            event.boardPostUser,
            NotificationTitleType.BOARD,
            NotificationContentBoardType.SCRAP,
            event.boardTitle
        )
    }

    @EventListener
    fun createBoardLikesEventHandler(event: CreateBoardLikesEvent) {
        notificationService.createBoardNotification(
            event.boardPostUser,
            NotificationTitleType.BOARD,
            NotificationContentBoardType.LIKES,
            event.boardTitle
        )
    }

    @EventListener
    fun changeUserKeywordEventHandler(event: ChangeUserKeywordEvent) {      //TODO
        notificationService.createMateNotification(
            event.user,
            NotificationTitleType.MATE,
            NotificationContentMateType.KEYWORD,
            event.mateRoleType
        )
    }

    @EventListener
    fun mateMatchSuccessEventHandler(event: MateMatchSuccessEvent) {        //TODO
        notificationService.createMateNotification(
            event.user,
            NotificationTitleType.MATE,
            NotificationContentMateType.MATCHING,
            null
        )
    }

    @EventListener
    fun provideRewardEventHandler(event: ProvideRewardEvent) {
        notificationService.createRewardNotification(
            event.user,
            NotificationTitleType.REWARD,
            event.rewardRequirementType
        )
    }

}