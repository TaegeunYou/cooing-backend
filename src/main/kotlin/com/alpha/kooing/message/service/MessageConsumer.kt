package com.alpha.kooing.message.service

import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.message.dto.UserMessage
import jakarta.transaction.Transactional
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MessageConsumer(
    val template: SimpMessagingTemplate,
    val chatRoomRepository: ChatRoomRepository,
){
    @Transactional
    @KafkaListener(topics = ["chat"], groupId = "foo")
    fun consume(@Payload msg: UserMessage){
        println("consume ${msg.content}")
        val res = decreaseUnreadCount(roomId = msg.roomId)
        if(res == null){
            println("존재하지 않는 채팅방")
        }
        val subscribePath = "/queue/chat/${msg.roomId}"
        return template.convertAndSend(subscribePath, msg.content)
    }

    fun decreaseUnreadCount(roomId: Long): Long?{
        val room = chatRoomRepository.findById(roomId).orElse(null)?:return null
        room.unreadChat-=1
        println(room.unreadChat)
        return roomId
    }
}