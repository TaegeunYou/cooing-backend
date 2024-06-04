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
import com.alpha.kooing.util.DateUtil
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
    fun findByUserList(sender:Long, userIdList:MutableList<Long>):List<ChatRoomResponseDto>?{
        val chatRoom = chatRoomRepository.findByUserList(userIdList)?:return null
        return chatRoom.map { convertEntityToResponseDto(sender, it) }
    }

    @Transactional
    fun createChatRoomByUserIdList(userIdList: MutableList<Long>):ChatRoom?{
        val savedChatRoom = chatRoomRepository.save(ChatRoom(0))

        userIdList.forEach {
            val user = userRepository.findById(it).orElse(null) ?: return null
            val chatMatching = ChatMatching(user, savedChatRoom)
            savedChatRoom.chatMatching.add(chatMatchingRepository.save(chatMatching))
        }
        return savedChatRoom
    }

    @Transactional
    fun convertEntityToResponseDto(sender:Long, chatRoom:ChatRoom):ChatRoomResponseDto{
        val receiverId = chatRoom.chatMatching.map {
            if(it.user.id != sender){
                it.user.id as Long
            }else{
                null
            }
        }.filterNotNull().getOrNull(0)?:throw Exception("잘못된 채팅방 정보에 접근")
        val unreadChatList = chatRepository.getUnreadChatByUserId(sender, chatRoom.id)
        val chatList = chatRepository.findByChatRoomId(chatRoom.id as Long)
        val lastChat = chatList.lastOrNull()
        val lastUpdate = lastChat?.createdAt?:LocalDateTime.now()
        return ChatRoomResponseDto(
            id = chatRoom.id,
            receiverId = receiverId,
            unreadChat = unreadChatList.size.toLong(),
            lastChat = lastChat?.content,
            lastUpdate = DateUtil.getDateTimeFormat(lastUpdate)
        )
    }

    @Transactional
    fun getOrCreateChatRoomByUserList(sender:Long, receiver:MutableList<Long>?):List<ChatRoomResponseDto>?{
        val userIdList = mutableListOf(sender)
        receiver?.forEach{ userIdList.add(it) }
        var chatRoom = chatRoomRepository.findByUserList(userIdList)
        if(chatRoom == null) {
            if(receiver == null) throw Exception("채팅방 정보가 없습니다")
            createChatRoomByUserIdList(userIdList)
            chatRoom = chatRoomRepository.findByUserList(userIdList)?:throw Exception("채팅방 생성 실패")
        }
        return chatRoom.map { convertEntityToResponseDto(sender, it) }
    }

    @Transactional
    fun getOrCreateMatchUserChatRooms(userId:Long):List<ChatRoomResponseDto>?{
        val matchUser = matchUserRepository.findByUserId(userId).map { it.matchUser }.orElse(null)?:return null
        val userIdList = mutableListOf(matchUser.id as Long, userId)
        var chatRoom = chatRoomRepository.findByUserList(userIdList)
        if(chatRoom == null){
            createChatRoomByUserIdList(userIdList)
            chatRoom = chatRoomRepository.findByUserList(userIdList)?:throw Exception("채팅방 생성 실패")
        }
        return chatRoom.map { convertEntityToResponseDto(userId, it) }
    }
}