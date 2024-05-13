package com.alpha.kooing.message.controller

import com.alpha.kooing.chat.service.ChatService
import com.alpha.kooing.message.dto.UserMessage
import com.alpha.kooing.message.service.MessageProducer
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(
    val producer: MessageProducer,
    val chatService: ChatService,
){
    @MessageMapping("/chatting")
    fun sendToUser(@Payload message: UserMessage){
        println("roomId : ${message.roomId}")
        val res = producer.sendTemplate("chatting", message)
        if(res == null){
            println("produce error")
        }else{
            chatService.save(message)
        }
    }

    @MessageMapping("/chat/{id}")
    fun readChat(@DestinationVariable("id") id:String, @Payload message:UserMessage){
        val chatId = id.toLong()
        chatService.decreaseUnread(chatId = chatId, senderId = message.senderId)?:return
        message.content = id
        producer.sendTemplate("chat", message)
    }
}