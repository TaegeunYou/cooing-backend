package com.alpha.kooing.user.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*

@Entity
class UserConcernKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "concern_keyword_id")
    val concernKeyword: ConcernKeyword,
)