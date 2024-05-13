package com.alpha.kooing.chat.entity

import com.alpha.kooing.chat.dto.ChatResponseDto
import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.user.entity.User
import com.alpha.kooing.util.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Chat(
    @Column(name = "content")
    var content:String,

    @Column(name= "unread")
    var unread:Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    var chatRoom: ChatRoom,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null
):BaseTimeEntity(){
    fun toResponseDto():ChatResponseDto{
        return ChatResponseDto(id, unread, user.id, chatRoom.id, content)
    }
}