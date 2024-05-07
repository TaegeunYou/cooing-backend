package com.alpha.kooing.board.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    var content: String,

    @CreatedDate
    val createDatetime: LocalDateTime
) {
    fun update(content: String) {
        this.content = content
    }
}