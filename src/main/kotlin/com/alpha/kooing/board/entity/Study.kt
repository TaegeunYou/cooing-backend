package com.alpha.kooing.board.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Study(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val title: String,

    val category: String,

    val location: String,

    val numberOfMembers: Int,

    val content: String,

    @CreatedDate
    val createDatetime: LocalDateTime
)