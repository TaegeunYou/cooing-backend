package com.alpha.kooing.chatRoom.service

import com.alpha.kooing.chat.entity.Chat
import com.alpha.kooing.chat.repository.ChatRepository
import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.chatMatching.repository.ChatMatchingRepository
import com.alpha.kooing.chatRoom.dto.ChatRoomResponseDto
import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.user.repository.MatchUserRepository
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ChatRoomService(
    val chatRoomRepository:ChatRoomRepository,
    val chatRepository: ChatRepository,
    val userRepository: UserRepository,
    val chatMatchingRepository: ChatMatchingRepository,
    val matchUserRepository: MatchUserRepository
){
    @Transactional
    fun findByUserList(userIdList:List<Long>):ChatRoomResponseDto?{
        val chatRoom = chatRoomRepository.findByUserList(userIdList)?:return null
        return ChatRoomResponseDto(
            id = chatRoom.id,
            unreadChat = chatRoom.unreadChat.toLong(),
            lastChat = "",
            lastUpdate = ""
        )
    }

    @Transactional
    fun createChatRoomByUserIdList(userIdList: List<Long>):ChatRoom?{
        val savedChatRoom = chatRoomRepository.save(ChatRoom(0))

        userIdList.forEach {
            val user = userRepository.findById(it).orElse(null) ?: return null
            val chatMatching = ChatMatching(user, savedChatRoom)
            savedChatRoom.chatMatching.add(chatMatchingRepository.save(chatMatching))
        }
        return savedChatRoom
    }


    @Transactional
    fun getOrCreateMatchUserChatRooms(userId:Long):ChatRoomResponseDto?{
        val matchUser = matchUserRepository.findByUserId(userId).map { it.matchUser }.orElse(null)?:return null
        val userIdList = listOf(matchUser.id as Long, userId)
        var chatRoom = chatRoomRepository.findByUserList(userIdList)
        if(chatRoom == null){
            chatRoom = createChatRoomByUserIdList(userIdList)?:return null
        }
        val unreadChatList = chatRepository.getUnreadChatByUserId(userId, chatRoom.id)
        val chatList = chatRepository.findByChatRoomId(chatRoom.id as Long)
        val lastChat = chatList.lastOrNull()
        val lastUpdate = lastChat?.createdAt?:LocalDateTime.now()
        return ChatRoomResponseDto(
            id = chatRoom.id,
            unreadChat = unreadChatList.size.toLong(),
            lastChat = lastChat?.content,
            lastUpdate = lastUpdate.format(DateTimeFormatter.ofPattern(
                if (lastUpdate.toLocalDate()!= LocalDate.now()) "MM-dd" else "HH:mm"
            ))
        )
    }
}