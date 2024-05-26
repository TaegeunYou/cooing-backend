package com.alpha.kooing.message.dto

import java.io.Serializable

class UserMessage(
    var content:String,
    val senderId:Long,
    val roomId:Long?,
    var chatId:Long?
):Serializable{
    override fun toString(): String {
        return "UserMessage(content='$content', senderId=$senderId, roomId=$roomId, chatId=$chatId)"
    }
}