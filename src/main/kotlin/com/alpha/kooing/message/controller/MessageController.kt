package com.alpha.kooing.message.controller

import com.alpha.kooing.chat.service.ChatService
import com.alpha.kooing.message.dto.ChatMessage
import com.alpha.kooing.message.dto.UserMessage
import com.alpha.kooing.message.service.MessageProducer
import com.alpha.kooing.util.DateUtil
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class MessageController(
    val producer: MessageProducer,
    val chatService: ChatService,
){
    @MessageMapping("/chatting")
    fun sendToUser(@Payload message: UserMessage){
        val chat = chatService.save(message)?:return
        message.chatId = chat.id
        message.createdAt = DateUtil.getDateTimeFormat(LocalDateTime.now())
        println(message.toString())
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
        val chat = chatService.findByChatId(message.id)?:throw Exception("채팅 정보가 없습니다.")
        val unread = chatService.decreaseUnread(chatId = message.id, senderId = message.senderId)?:throw Exception("읽음 표시 실패")
        message.unread = unread
        message.createAt = chat.createdAt
        producer.sendTemplate("chat", message)
    }
}