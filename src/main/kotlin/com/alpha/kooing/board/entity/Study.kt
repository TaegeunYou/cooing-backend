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

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val category: String,

    @Column(nullable = false)
    val location: String,

    @Column(nullable = false)
    val numberOfMembers: Int,

    @Column(nullable = false)
    val content: String,

    @CreatedDate
    @Column(nullable = false)
    val createDatetime: LocalDateTime
)