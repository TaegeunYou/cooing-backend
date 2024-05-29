package com.alpha.kooing.chatRoom.service

import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.chatMatching.repository.ChatMatchingRepository
import com.alpha.kooing.chatRoom.dto.ChatRoomResponseDto
import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.user.repository.MatchUserRepository
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrDefault

@Service
class ChatRoomService(
    val chatRoomRepository:ChatRoomRepository,
    val userRepository: UserRepository,
    val chatMatchingRepository: ChatMatchingRepository,
    val matchUserRepository: MatchUserRepository
){
    @Transactional
    fun findByUserList(userIdList:List<Long>):ChatRoomResponseDto?{
        val chatRoom = chatRoomRepository.findByUserList(userIdList)?:return null
        return chatRoom.toResponseDto()
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
    fun getOrCreateMatchUserChatRooms(userId:Long):List<ChatRoomResponseDto>?{
        val matchUsers = matchUserRepository.findAllByUserId(userId).map { it.matchUser }
        val chatRoomList = matchUsers.map{
            val userIdList = listOf(userId, it.id as Long)
            val chatRoom = chatRoomRepository.findByUserList(userIdList)
            if(chatRoom == null){
                val savedChatRoom = createChatRoomByUserIdList(userIdList)?:return null
                savedChatRoom
            }else{
                chatRoom
            }
        }
        return chatRoomList.map { it.toResponseDto() }
    }
}