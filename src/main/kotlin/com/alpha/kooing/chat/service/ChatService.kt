package com.alpha.kooing.chat.service

import com.alpha.kooing.chat.dto.ChatResponseDto
import com.alpha.kooing.chat.entity.Chat
import com.alpha.kooing.chat.repository.ChatRepository
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.message.dto.UserMessage
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ChatService(
    val chatRoomRepository: ChatRoomRepository,
    val userRepository: UserRepository,
    val chatRepository: ChatRepository,
){
    @Transactional
    fun save(message:UserMessage):Chat?{
        println("msg : ${message.content} and sender : ${message.senderId}")
        if(message.roomId == null) return null
        val chatRoom = chatRoomRepository.findById(message.roomId).get()
        val user = userRepository.findById(message.senderId).get()
        val chat = Chat(content = message.content, chatRoom = chatRoom, user = user, unread = 1)
        return chatRepository.save(chat)
    }

    @Transactional
    fun findByRoomId(roomId:Long):List<ChatResponseDto>?{
        val chatList = chatRepository.findByChatRoomId(roomId)
        return chatList.map { it.toResponseDto() }
    }

    @Transactional
    fun decreaseUnread(chatId:Long, senderId:Long):Int?{
        val chat = chatRepository.findById(chatId).orElse(null)?:return null
        if(chat.user.id == senderId) return null
        if(chat.unread>=1){
            chat.unread-=1
        }
        return chat.unread
    }

    @Transactional
    fun deleteById(chatId:Long?):Boolean{
        if(chatId == null) return false
        return try {
            chatRepository.deleteById(chatId)
            true
        }catch (e:Exception){
            false
        }
    }
}