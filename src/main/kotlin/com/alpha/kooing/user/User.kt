package com.alpha.kooing.user

import com.alpha.kooing.chatMatching.entity.ChatMatching
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
)