package com.alpha.kooing.chatRoom.controller

import com.alpha.kooing.chatRoom.service.ChatRoomService
import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chatroom")
class ChatRoomController(
    val chatRoomService: ChatRoomService,
    val jwtTokenProvider: JwtTokenProvider
){
    @GetMapping("")
    fun createOrFindRoom(@RequestParam userIdList:List<Long>): ApiResponse<*> {
        var chatRoom = chatRoomService.findByUserList(userIdList)
        if(chatRoom == null) {
            val newChatRoom = chatRoomService.createChatRoomByUserIdList(userIdList)
                ?:return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
            chatRoom = newChatRoom.toResponseDto()
        }
        return ApiResponse(HttpStatus.OK.name, chatRoom)
    }

    @GetMapping("/match")
    fun getMatchUserChatRoom(request: HttpServletRequest):ApiResponse<*>{
        val token = jwtTokenProvider.resolveToken(request)
        val userId = jwtTokenProvider.getJwtUserId(token).toLong()
        val chatRoomList = chatRoomService.getOrCreateMatchUserChatRooms(userId)
            ?:return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        return ApiResponse(HttpStatus.OK.name, chatRoomList)
    }
}