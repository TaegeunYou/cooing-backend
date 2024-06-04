package com.alpha.kooing.chat.controller

import com.alpha.kooing.chat.service.ChatService
import com.alpha.kooing.common.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    val chatService: ChatService
){
    @GetMapping("/{roomId}")
    fun findByRoomId(@PathVariable("roomId") roomId:Long): ApiResponse<*> {
        val res = chatService.findByRoomId(roomId)
        return if (res != null){
            ApiResponse(HttpStatus.OK.name, res)
        }else{
            ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        }
    }
}