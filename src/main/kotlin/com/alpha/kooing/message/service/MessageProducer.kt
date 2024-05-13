package com.alpha.kooing.message.service

import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.message.dto.UserMessage
import jakarta.transaction.Transactional
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageProducer(
    val kafkaTemplate: KafkaTemplate<String, UserMessage>,
    val chatRoomRepository: ChatRoomRepository,
){
    @Transactional
    fun sendToUser(message:UserMessage): UserMessage?{
        println(message.content)
        val future = kafkaTemplate.send("chat", message)
        val ret = future.handle { _, error ->
            if(error != null){
                null
            }else if( increaseUnreadCount(message.roomId) == null){
                null
            }else{
                message
            }
        }.get()
        return ret
    }

    fun increaseUnreadCount(roomId:Long):Long?{
        val room = chatRoomRepository.findById(roomId).orElse(null)?:return null
        room.unreadChat+=1
        return roomId
    }
}