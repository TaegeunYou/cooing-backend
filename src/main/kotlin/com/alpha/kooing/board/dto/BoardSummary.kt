package com.alpha.kooing.board.dto

import com.alpha.kooing.board.entity.Board

class BoardSummary(
    val boardId: Long,
    val title: String,  //제목
    val contentSummary: String, //내용 첫 문장
    val createDatetime: String, //생성 날짜
    val commentCount: Int   //댓글 개수
)