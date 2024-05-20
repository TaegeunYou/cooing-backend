package com.alpha.kooing.user.entity

import com.alpha.kooing.board.entity.Board
import com.alpha.kooing.board.entity.Study
import com.alpha.kooing.user.User
import jakarta.persistence.*

@Entity
class ConcernKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToMany(mappedBy = "concernKeyword", fetch = FetchType.LAZY)
    val userConcernKeyword: MutableList<UserConcernKeyword> = mutableListOf(),

    @Column(nullable = false)
    val name: String
)