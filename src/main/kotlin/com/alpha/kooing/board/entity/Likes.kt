package com.alpha.kooing.board.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*

@Entity
class Likes(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
)