package com.alpha.kooing.board.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    val likes: MutableList<Likes> = mutableListOf(),

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    val scraps: MutableList<Scrap> = mutableListOf(),

    var title: String,

    var content: String,

    @CreatedDate
    val createDatetime: LocalDateTime
) {
    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }
}