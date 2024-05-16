package com.alpha.kooing.board.dto

class BoardDetail(
    val boardId: Long,
    val title: String,  //제목
    val content: String, //내용
    val likesCount: Int,   //좋아요 개수
    val commentCount: Int,   //댓글 개수
    val scrapCount: Int,   //스크랩 개수
    val createDatetime: String, //생성 날짜
    val createUserId: Long,
    val createUserName: String, //생성 사람 이름
    val createUserIconUrl: String,  //생성 사람 프로필 사진
    val comments: List<BoardDetailComment>  //댓글
) {
    class BoardDetailComment(
        val commentId: Long,
        val createUserName: String,
        val content: String,
        val createDatetime: String
    )
}