package com.alpha.kooing.board.controller

import com.alpha.kooing.board.dto.*
import com.alpha.kooing.board.enum.BoardType
import com.alpha.kooing.board.service.BoardService
import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BoardController(
    val boardService: BoardService,
    val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/boards")
    fun getBoards(
        httpServletRequest: HttpServletRequest,
        @RequestParam("category") category: BoardType?,
        @RequestParam("query") query: String?,
    ): ResponseEntity<ApiResponse<List<BoardSummary>>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                boardService.getBoards(token, category, query)
            )
        )
    }

    @GetMapping("/board/{boardId}")
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
    fun createBoard(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: CreateBoardRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.createBoard(token, request)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/board/{boardId}")
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
    fun deleteBoard(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.deleteBoard(token, boardId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/board/{boardId}/comment")
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
    fun createLikes(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.createLikes(token, boardId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/board/{boardId}/likes")
    fun deleteLikes(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.deleteLikes(token, boardId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/board/{boardId}/scrap")
    fun createScrap(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.createScrap(token, boardId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/board/{boardId}/scrap")
    fun deleteScrap(
        httpServletRequest: HttpServletRequest,
        @PathVariable("boardId") boardId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        boardService.deleteScrap(token, boardId)
        return ResponseEntity.ok().build()
    }
}