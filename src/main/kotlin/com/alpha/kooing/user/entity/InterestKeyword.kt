package com.alpha.kooing.user.entity

import jakarta.persistence.*

@Entity
class InterestKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToMany(mappedBy = "interestKeyword", fetch = FetchType.LAZY)
    val userInterestKeyword: MutableList<UserInterestKeyword> = mutableListOf(),

    @Column(nullable = false)
    val name: String
)