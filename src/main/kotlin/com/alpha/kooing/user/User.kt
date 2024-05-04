package com.alpha.kooing.user

import jakarta.persistence.*

@Entity
class User(
    @Column(name = "email", nullable = false)
    val email:String,

    @Column(name = "name", nullable = false)
    var username:String,

    @Column(name = "role", nullable = false)
    val role: Role,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
)