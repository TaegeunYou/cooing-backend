package com.alpha.kooing.message.controller

import com.alpha.kooing.chat.service.ChatService
import com.alpha.kooing.message.dto.ChatMessage
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
        val chat = chatService.save(message)?:return
        message.chatId = chat.id
        val res = producer.sendTemplate("chatting", message)
        if(res == null){
            println("produce error")
            chatService.deleteById(chatId = chat.id)
        }else{
            println("produce success")
        }
    }

    @MessageMapping("/chat/{id}")
    fun readChat(@DestinationVariable("id") id:String, @Payload message:ChatMessage){
        val unread = chatService.decreaseUnread(chatId = message.id, senderId = message.senderId)?:return
        message.unread = unread
        producer.sendTemplate("chat", message)
    }
}