package com.alpha.kooing.chatRoom.repository

interface CustomChatRoomRepository {
    fun findByUserList(userIdList:List<String>): MutableList<*>?;
}