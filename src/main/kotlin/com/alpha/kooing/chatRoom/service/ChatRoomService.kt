package com.alpha.kooing.chatRoom.service

import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.chatMatching.repository.ChatMatchingRepository
import com.alpha.kooing.chatRoom.dto.ChatRoomResponseDto
import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatRoomService(
    val chatRoomRepository:ChatRoomRepository,
    val userRepository: UserRepository,
    val chatMatchingRepository: ChatMatchingRepository
){
    @Transactional
    fun findByUserList(userIdList:List<String>):ChatRoomResponseDto?{
        val chatRoomList = chatRoomRepository.findByUserList(userIdList)
        if(chatRoomList==null || chatRoomList.isEmpty()){
            return null
        }
        val chatRoom = chatRoomList[0] as ChatRoom
        return chatRoom.toResponseDto()
    }

    @Transactional
    fun getChatRoom(userIdList: List<String>):ChatRoom?{
        val chatRoom = ChatRoom(0)
        val savedChatRoom = chatRoomRepository.save(chatRoom)

        userIdList.map {
            val user = userRepository.findById(it.toLong()).orElse(null) ?: return null
            val chatMatching = ChatMatching(user, savedChatRoom)
            savedChatRoom.chatMatching.add(chatMatchingRepository.save(chatMatching))
        }
        return savedChatRoom
    }
}