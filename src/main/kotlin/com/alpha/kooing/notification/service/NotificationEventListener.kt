package com.alpha.kooing.notification.service

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class NotificationEventListener(
    private val notificationEventService: NotificationEventService
) {

    @TransactionalEventListener
    fun matchSuccessEventHandler(userId: Long) {
//        notificationEventService.createReward
//        sesService.sendEmail(event.email)
        TODO()
    }

}