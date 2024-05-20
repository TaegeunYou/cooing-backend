package com.alpha.kooing.userMatching.entity

import com.alpha.kooing.matching.entity.Matching
import com.alpha.kooing.user.User
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
@IdClass(UserMatchingId::class)
class UserMatching(
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Id
    @ManyToOne
    @JoinColumn(name = "matching_id")
    val matching:Matching
)