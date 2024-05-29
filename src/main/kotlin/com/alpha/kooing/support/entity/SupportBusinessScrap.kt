package com.alpha.kooing.support.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*

@Entity
class SupportBusinessScrap(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "support_business_id")
    val supportBusiness: SupportBusiness,
)