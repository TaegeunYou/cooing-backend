package com.alpha.kooing.matching.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Matching(
    @Column(name = "name")
    val name: String,

    @Column(name = "type")
    val type: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?
)