package com.alpha.kooing.board.controller

import com.alpha.kooing.board.dto.*
import com.alpha.kooing.board.enum.BoardType
import com.alpha.kooing.board.service.BoardService
import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BoardController(
    private val boardService: BoardService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/boards")
    @Operation(summary = "게시물 전체 조회", description = "자유게시판, 내가 쓴 글, 댓글 단 글, 스크랩한 글이나 검색어를 통해 조회합니다.")
    fun getBoards(
        httpServletRequest: HttpServletRequest,
        @RequestParam("boardType") boardType: BoardType?,
        @RequestParam("query") query: String?,
    ): ResponseEntity<ApiResponse<List<BoardSummary>>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                boardService.getBoards(token, boardType, query)
            )
        )
    }

    @GetMapping("/board/{boardId}")
    @Operation(summary = "게시물 상세 조회")
    fun getBoardDetail(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<BoardDetail>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                boardService.getBoardDetail(token, boardId)
            )
        )
    }

    @PostMapping("/board")
    @Operation(summary = "게시물 작성")
    fun createBoard(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: CreateBoardRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.createBoard(token, request)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/board/{boardId}")
    @Operation(summary = "게시물 수정")
    fun updateBoard(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long,
        @RequestBody request: UpdateBoardRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.updateBoard(token, boardId, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/board/{boardId}")
    @Operation(summary = "게시물 삭제")
    fun deleteBoard(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.deleteBoard(token, boardId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/board/{boardId}/comment")
    @Operation(summary = "댓글 작성")
    fun createComment(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long,
        @RequestBody request: CreateCommentRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.createComment(token, boardId, request)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/board/{boardId}/comment/{commentId}")
    @Operation(summary = "댓글 수정")
    fun updateComment(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long,
        @PathVariable("commentId") commentId: Long,
        @RequestBody request: UpdateCommentRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.updateComment(token, boardId, commentId, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    @Operation(summary = "댓글 삭제")
    fun deleteComment(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long,
        @PathVariable("commentId") commentId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.deleteComment(token, boardId, commentId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/board/{boardId}/likes")
    @Operation(summary = "게시물 좋아요")
    fun createLikes(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.createLikes(token, boardId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/board/{boardId}/likes")
    @Operation(summary = "게시물 좋아요 취소")
    fun deleteLikes(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.deleteLikes(token, boardId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/board/{boardId}/scrap")
    @Operation(summary = "게시물 스크랩")
    fun createScrap(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.createScrap(token, boardId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/board/{boardId}/scrap")
    @Operation(summary = "게시물 스크랩 취소")
    fun deleteScrap(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.deleteScrap(token, boardId)
        return ResponseEntity.ok().build()
    }
}