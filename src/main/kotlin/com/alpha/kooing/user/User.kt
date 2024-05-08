package com.alpha.kooing.user

import com.alpha.kooing.board.entity.*
import jakarta.persistence.*

@Entity
class User(
    @Column(name = "email", nullable = false)
    val email:String,

    @Column(name = "name", nullable = false)
    var username:String,

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    val role: Role,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val boards: MutableList<Board> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val scraps: MutableList<Scrap> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val clubs: MutableList<Club> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val studies: MutableList<Study> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
)