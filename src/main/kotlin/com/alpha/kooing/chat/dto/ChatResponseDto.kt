package com.alpha.kooing.chat.dto

class ChatResponseDto(
    val id:Long?,
    val unread:Int,
    val userId:Long?,
    val chatRoomId:Long?,
    val content:String,
    val createdAt:String
)