package com.alpha.kooing.message.service

import com.alpha.kooing.message.dto.UserMessage
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MessageConsumer(
    val template: SimpMessagingTemplate
){
    @KafkaListener(topics = ["chat"], groupId = "foo")
    fun consume(@Payload msg: UserMessage){
        println("consume ${msg.content}")
        return template.convertAndSend("/queue/chat" + msg.roomId, msg)
    }
}