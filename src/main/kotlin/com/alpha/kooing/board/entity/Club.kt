package com.alpha.kooing.board.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Club(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val title: String,

    val summary: String,

    val imageUrl: String?,

    val recruitStartDate: LocalDate,

    val recruitEndDate: LocalDate,

    val content: String,
)