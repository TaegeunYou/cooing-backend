package com.alpha.kooing.board.event

import com.alpha.kooing.user.User

class CreateBoardLikesEvent(
    val boardPostUser: User,
    val boardTitle: String
)