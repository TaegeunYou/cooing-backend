package com.alpha.kooing.message.dto

import java.io.Serializable

class UserMessage(
    val roomId: Long,
    val senderId: Long,
    val content: String,
):Serializable