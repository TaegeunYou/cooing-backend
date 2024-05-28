package com.alpha.kooing.support.entity

import com.alpha.kooing.user.User
import jakarta.persistence.*

@Entity
class JobPostingScrap(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "job_posting_id")
    val jobPosting: JobPosting,
)