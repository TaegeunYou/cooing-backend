package com.alpha.kooing.message.dto

import java.time.LocalDate

class ChatMessage(
    var id:Long,
    var senderId: Long,
    var unread: Int,
    var createAt: String
)