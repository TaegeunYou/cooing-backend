package com.alpha.kooing.message.service

import com.alpha.kooing.message.dto.UserMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageProducer(
    val kafkaTemplate: KafkaTemplate<String, UserMessage>
){
    fun sendToUser(message:UserMessage){
        kafkaTemplate.send("chat", message)
    }
}