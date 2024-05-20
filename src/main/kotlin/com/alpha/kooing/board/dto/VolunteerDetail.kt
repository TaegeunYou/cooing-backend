package com.alpha.kooing.board.dto

class VolunteerDetail(
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val recruitStartDate: String,
    val recruitEndDate: String,
    val content: String,
    val createUserId: Long
)