package com.alpha.kooing.notification.service

import com.alpha.kooing.common.Utils
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.notification.dto.GetNotificationsResponse
import com.alpha.kooing.notification.entity.Notification
import com.alpha.kooing.notification.enum.NotificationContentBoardType
import com.alpha.kooing.notification.enum.NotificationContentMateType
import com.alpha.kooing.notification.enum.NotificationContentRewardType
import com.alpha.kooing.notification.enum.NotificationTitleType
import com.alpha.kooing.notification.repository.NotificationRepository
import com.alpha.kooing.reward.enum.RewardRequirementType
import com.alpha.kooing.user.User
import com.alpha.kooing.user.enum.RoleType
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class NotificationService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) {

    @Transactional(readOnly = true)
    fun getNotifications(token: String): GetNotificationsResponse {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        val userNotification = notificationRepository.findAllByUserId(user.id!!)
        return GetNotificationsResponse(
            userNotification
                .sortedByDescending { it.id }
                .map { item ->
                    GetNotificationsResponse.NotificationItem(
                        item.title,
                        item.content,
                        Utils.dateTimeToFrontFormat(item.createdAt)
                    )
                }
        )
    }

    @Transactional
    fun createBoardNotification(
        user: User,
        notificationTitleType: NotificationTitleType,
        notificationContentBoardType: NotificationContentBoardType,
        boardTitle: String,
    ) {
        notificationRepository.save(
            Notification(
                null,
                user,
                notificationTitleType.desc,
                "'$boardTitle'" + notificationContentBoardType.desc
            )
        )
    }

    @Transactional
    fun createMateNotification(
        user: User,
        notificationTitleType: NotificationTitleType,
        notificationContentMateType: NotificationContentMateType,
        mateRoleType: RoleType?,
    ) {
        notificationRepository.save(
            Notification(
                null,
                user,
                notificationTitleType.desc,
                when (notificationContentMateType) {
                    NotificationContentMateType.MATCHING -> notificationContentMateType.desc
                    NotificationContentMateType.KEYWORD -> {
                        mateRoleType!!.name + notificationContentMateType.desc
                    }
                }
            )
        )
    }

    @Transactional
    fun createRewardNotification(
        user: User,
        notificationTitleType: NotificationTitleType,
        rewardRequirementType: RewardRequirementType
    ) {
        notificationRepository.save(
            Notification(
                null,
                user,
                notificationTitleType.desc,
                rewardRequirementType.name.replace("_", " ") + " 리워드가 지급되었습니다."
            )
        )
    }
}