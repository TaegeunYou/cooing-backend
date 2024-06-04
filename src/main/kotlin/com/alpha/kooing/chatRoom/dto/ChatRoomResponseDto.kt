package com.alpha.kooing.chatRoom.dto

class ChatRoomResponseDto(
    val id:Long?,
    val receiverId: Long,
    val unreadChat:Long,
    val lastChat:String?,
    val lastUpdate:String?
)