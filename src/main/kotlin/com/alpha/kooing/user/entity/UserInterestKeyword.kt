package com.alpha.kooing.user.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*

@Entity
class UserInterestKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "interest_keyword_id")
    val interestKeyword: InterestKeyword,
)