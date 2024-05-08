package com.alpha.kooing.chatMatching.entity

import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.user.entity.User
import jakarta.persistence.*

@Entity
class ChatMatching(
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    val chatRoom: ChatRoom,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
)