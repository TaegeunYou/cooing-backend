package com.alpha.kooing.chatRoom.entity

import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.chatRoom.dto.ChatRoomResponseDto
import com.alpha.kooing.util.BaseTimeEntity
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
class ChatRoom(
    @Column(name = "unreadChat")
    var unreadChat:Long,

    @OneToMany
    @JoinColumn(name = "chat_room_id")
    var chatMatching: MutableList<ChatMatching> = mutableListOf(),

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null
):BaseTimeEntity(){
    fun toResponseDto():ChatRoomResponseDto{
        return ChatRoomResponseDto(id, unreadChat)
    }
}