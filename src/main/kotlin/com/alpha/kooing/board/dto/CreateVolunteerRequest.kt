package com.alpha.kooing.board.dto

class CreateVolunteerRequest(
    val title: String,
    val summary: String,
    val recruitDate: String,        // ex)04.01 ~ 04.23
    val content: String
)