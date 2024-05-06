package com.alpha.kooing.chatRoom.entity

import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.chatRoom.dto.ChatRoomResponseDto
import com.alpha.kooing.util.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
class ChatRoom(
    @Column(name = "unreadChat")
    var unreadChat:Long,

    @OneToMany
    var chatMatching: List<ChatMatching> = listOf(),

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null
):BaseTimeEntity(){
    fun toResponseDto():ChatRoomResponseDto{
        return ChatRoomResponseDto(id, unreadChat)
    }
}