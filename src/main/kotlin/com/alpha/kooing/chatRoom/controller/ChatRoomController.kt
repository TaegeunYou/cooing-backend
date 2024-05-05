package com.alpha.kooing.chatRoom.controller

import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.chatRoom.service.ChatRoomService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatRoomController(
    val chatRoomService: ChatRoomService
){
    @GetMapping("/test")
    fun test():ChatRoom{
        val chatRoom = chatRoomService.findByUserList(listOf()) ?: return ChatRoom(-1, -1)
        return chatRoom
    }
}