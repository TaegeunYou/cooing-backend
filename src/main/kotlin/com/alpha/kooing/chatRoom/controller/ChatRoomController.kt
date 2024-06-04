package com.alpha.kooing.chatRoom.controller

import com.alpha.kooing.chatRoom.dto.ChatRoomResponseDto
import com.alpha.kooing.chatRoom.service.ChatRoomService
import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class ChatRoomController(
    val chatRoomService: ChatRoomService,
    val jwtTokenProvider: JwtTokenProvider
){
    @GetMapping("/chatroom")
    @Operation(summary = "사용자 목록으로 채팅방 생성 / 없으면 새로 생성해서 제공")
    fun createOrFindRoom(@RequestParam sender:Long, @RequestParam receiver:MutableList<Long>?): ApiResponse<*> {
        val chatRoom = chatRoomService.getOrCreateChatRoomByUserList(sender, receiver)
        return ApiResponse(HttpStatus.OK.name, chatRoom)
    }

    @GetMapping("chatroom/match")
    @Operation(summary = "매칭된 사용자와의 채팅방 조회")
    fun getMatchUserChatRoom(request: HttpServletRequest):ApiResponse<*>{
        val token = jwtTokenProvider.resolveToken(request)
        val userId = jwtTokenProvider.getJwtUserId(token).toLong()
        val chatRoomList = chatRoomService.getOrCreateMatchUserChatRooms(userId)
            ?:return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        return ApiResponse(HttpStatus.OK.name, chatRoomList)
    }
}