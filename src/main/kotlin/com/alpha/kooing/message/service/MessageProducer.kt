package com.alpha.kooing.message.service

import com.alpha.kooing.chat.entity.Chat
import com.alpha.kooing.chat.repository.ChatRepository
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.message.dto.UserMessage
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageProducer(
    val kafkaTemplate: KafkaTemplate<String, UserMessage>,
){
    fun sendToUser(message:UserMessage): UserMessage?{
        val future = kafkaTemplate.send("chat", message)
        val ret = future.handle { _, error ->
            if(error != null){
                null
            }else{
                message
            }
        }.get()
        return ret
    }
}