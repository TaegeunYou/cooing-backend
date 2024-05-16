package com.alpha.kooing.board.dto

class CreateStudyRequest(
    val title: String,
    val category: String,
    val location: String,
    val numberOfMembers: Int,
    val content: String,
)