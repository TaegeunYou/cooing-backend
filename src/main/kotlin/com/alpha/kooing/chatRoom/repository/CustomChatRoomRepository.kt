package com.alpha.kooing.chatRoom.repository

import com.alpha.kooing.chatRoom.entity.ChatRoom

interface CustomChatRoomRepository {
    fun findByUserList(userIdList:List<Long?>): List<ChatRoom>?;
}