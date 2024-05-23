package com.alpha.kooing.board.dto

class CreateClubRequest(
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val recruitDate: String,    // ex)04.01 ~ 04.23
    val content: String
)