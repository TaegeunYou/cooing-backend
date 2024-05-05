package com.alpha.kooing.chatRoom.service

import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.user.User
import org.springframework.stereotype.Service

@Service
class ChatRoomService(
    val chatRoomRepository:ChatRoomRepository
){
    fun findByUserList(user:List<User>):ChatRoom?{
        val chatRoomList = chatRoomRepository.findByUserList(user)
        if(chatRoomList==null || chatRoomList.isEmpty()){
            return null
        }
        return chatRoomList[0] as ChatRoom;
    }
}