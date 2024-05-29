package com.alpha.kooing.notification.entity

import com.alpha.kooing.user.User
import com.alpha.kooing.util.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val content: String,
) : BaseTimeEntity()