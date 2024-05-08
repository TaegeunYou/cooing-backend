package com.alpha.kooing.config

import com.alpha.kooing.chat.entity.Chat
import com.alpha.kooing.chat.repository.ChatRepository
import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.chatMatching.repository.ChatMatchingRepository
import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.chatRoom.repository.ChatRoomRepository
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.entity.User
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    val userRepository: UserRepository,
    val chatRoomRepository: ChatRoomRepository,
    val chatMatchingRepository: ChatMatchingRepository,
    val chatRepository: ChatRepository,
){
    @EventListener(ApplicationReadyEvent::class)
    fun initData(){
        val userList = createUser()
        val chatRoomList:MutableList<ChatRoom> = mutableListOf()
        for( user in userList){
            chatRoomList.add(createChatRoom(user))
        }
        for( chatRoom in chatRoomList){
            createChat(chatRoom)
        }
    }

    fun createUser():MutableList<User>{
        val res:MutableList<User> = mutableListOf()
        res.addLast(userRepository.save(User("kym8821","kym8821", Role.USER)))
        res.addLast(userRepository.save(User("louie9798","louie9798", Role.USER)))
        res.addLast(userRepository.save(User("postman","postman", Role.USER)))
        return res
    }

    fun createChatRoom(user:User):ChatRoom{
        val chatRoom = ChatRoom(0)
        val savedChatRoom = chatRoomRepository.save(chatRoom)
        val chatMatching = ChatMatching(user=user, chatRoom)
        val savedChatMatching = chatMatchingRepository.save(chatMatching)
        savedChatRoom.chatMatching.add(savedChatMatching)
        return savedChatRoom
    }

    fun createChat(chatRoom: ChatRoom) {
        val user = chatRoom.chatMatching[0].user
        val chat = Chat(user=user, chatRoom=chatRoom, content = "${user.username} ${chatRoom.createdAt}")
        chatRepository.save(chat)
    }
}