package com.alpha.kooing.chatRoom.controller

import com.alpha.kooing.chatRoom.service.ChatRoomService
import com.alpha.kooing.common.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chatroom")
class ChatRoomController(
    val chatRoomService: ChatRoomService
){
    @GetMapping("")
    fun createOrFindRoom(@RequestParam userId:List<String>): ApiResponse<*> {
        var chatRoom = chatRoomService.findByUserList(userId)
        if(chatRoom == null) {
            val newChatRoom = chatRoomService.getChatRoom(userId)
                ?:return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
            chatRoom = newChatRoom.toResponseDto()
        }
        return ApiResponse(HttpStatus.OK.name, chatRoom)
    }
}