package com.alpha.kooing.message.controller

import com.alpha.kooing.message.dto.UserMessage
import com.alpha.kooing.message.service.MessageProducer
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class MessageController(
    val producer: MessageProducer
){
    @MessageMapping("/chat")
    fun sendToUser(@Payload message: UserMessage, principal: Principal, accessor: SimpMessageHeaderAccessor){
        producer.sendToUser(message)
    }
}