package com.alpha.kooing.board.dto

class CreateClubRequest(
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val recruitStartDate: String,
    val recruitEndDate: String,
    val content: String
)