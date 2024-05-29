package com.alpha.kooing.board.event

import com.alpha.kooing.user.User

class CreateBoardCommentEvent(
    val boardPostUser: User,
    val boardTitle: String
)