package com.alpha.kooing.notification.service

import com.alpha.kooing.common.Utils
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.notification.dto.GetNotificationsResponse
import com.alpha.kooing.notification.repository.NotificationRepository
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
            userNotification.map { item ->
                GetNotificationsResponse.NotificationItem(
                    item.title,
                    item.content,
                    Utils.dateTimeToFrontFormat(item.createdAt)
                )
            }
        )
    }
}