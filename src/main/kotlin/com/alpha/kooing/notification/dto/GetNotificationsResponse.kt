package com.alpha.kooing.notification.dto

class GetNotificationsResponse(
    val item: List<NotificationItem>,
) {
    class NotificationItem(
        val title: String,
        val content: String,
        val createDatetime: String
    )
}