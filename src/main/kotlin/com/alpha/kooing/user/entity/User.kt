package com.alpha.kooing.user.entity

import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.UserResponseDto
import jakarta.persistence.*

@Entity
class User(
    @Column(name = "email", nullable = false)
    val email:String,

    @Column(name = "name", nullable = false)
    var username:String,

    @Column(name = "role", nullable = false)
    val role: Role,

    @OneToMany
    @JoinColumn(name = "user_id")
    var chatMatching: List<ChatMatching> = listOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
){
    fun toResponseDto():UserResponseDto{
        return UserResponseDto(
            id=this.id,
            email = this.email,
            username = this.username,
            role = this.role
        )
    }

    override fun toString(): String {
        return "User(email='$email', username='$username', role=$role)"
    }

}