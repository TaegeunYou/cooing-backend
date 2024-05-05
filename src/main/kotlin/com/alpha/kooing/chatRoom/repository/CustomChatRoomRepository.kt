package com.alpha.kooing.chatRoom.repository

import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.user.User

interface CustomChatRoomRepository {
    fun findByUserList(userList:List<User>): MutableList<*>?;
}