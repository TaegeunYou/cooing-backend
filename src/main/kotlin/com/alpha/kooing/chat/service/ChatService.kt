package com.alpha.kooing.chat.service

import com.alpha.kooing.chat.dto.ChatResponseDto
import com.alpha.kooing.chat.entity.Chat
import com.alpha.kooing.chat.repository.ChatRepository
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.message.dto.UserMessage
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatService(
    val chatRoomRepository: ChatRoomRepository,
    val userRepository: UserRepository,
    val chatRepository: ChatRepository,
){
    @Transactional
    fun save(message:UserMessage){
        println("msg : " + message.content)
        val chatRoom = chatRoomRepository.findById(message.roomId).get()
        val user = userRepository.findById(message.senderId).get()
        val chat = Chat(content = message.content, chatRoom = chatRoom, user = user)
        chatRepository.save(chat)
    }

    @Transactional
    fun findByRoomId(roomId:Long):List<ChatResponseDto>?{
        val chatList = chatRepository.findByChatRoomId(roomId)
        return chatList.map { it.toResponseDto() }
    }
}