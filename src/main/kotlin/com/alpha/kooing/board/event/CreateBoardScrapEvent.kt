package com.alpha.kooing.board.event

import com.alpha.kooing.user.User

class CreateBoardScrapEvent(
    val boardPostUser: User,
    val boardTitle: String
)