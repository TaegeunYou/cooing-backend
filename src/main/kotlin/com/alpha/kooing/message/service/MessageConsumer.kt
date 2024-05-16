package com.alpha.kooing.message.service

import com.alpha.kooing.message.dto.ChatMessage
import com.alpha.kooing.message.dto.UserMessage
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MessageConsumer(
    val template: SimpMessagingTemplate
){
    @KafkaListener(topics = ["chatting"], groupId = "foo")
    fun sendToUser(@Payload msg: UserMessage){
        println("consume ${msg.content}")
        val subscribePath = "/queue/chatting/${msg.roomId}"
        return template.convertAndSend(subscribePath, msg)
    }
    @KafkaListener(topics = ["chat"], groupId = "foo")
    fun handleUnread(@Payload msg:ChatMessage){
        val subscribePath = "/queue/chat/"
        return template.convertAndSend(subscribePath, msg)
    }
}