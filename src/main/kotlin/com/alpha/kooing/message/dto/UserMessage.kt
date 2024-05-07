package com.alpha.kooing.message.dto

import java.io.Serializable

class UserMessage(
    val roomId: Long,
    val content: String
):Serializable