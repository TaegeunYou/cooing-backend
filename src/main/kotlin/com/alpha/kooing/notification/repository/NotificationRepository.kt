package com.alpha.kooing.notification.repository

import com.alpha.kooing.notification.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository: JpaRepository<Notification, Long> {

    fun findAllByUserId(userId: Long): List<Notification>
}