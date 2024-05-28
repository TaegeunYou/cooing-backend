package com.alpha.kooing.board.service

import com.alpha.kooing.board.dto.*
import com.alpha.kooing.board.entity.Board
import com.alpha.kooing.board.entity.Comment
import com.alpha.kooing.board.entity.Likes
import com.alpha.kooing.board.entity.Scrap
import com.alpha.kooing.board.enum.BoardType
import com.alpha.kooing.board.event.*
import com.alpha.kooing.board.repository.BoardRepository
import com.alpha.kooing.board.repository.CommentRepository
import com.alpha.kooing.board.repository.LikesRepository
import com.alpha.kooing.board.repository.ScrapRepository
import com.alpha.kooing.common.Utils
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.User
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class BoardService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val likesRepository: LikesRepository,
    private val scrapRepository: ScrapRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional(readOnly = true)
    fun getBoards(token: String, boardType: BoardType?, query: String?): List<BoardSummary> {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        println(userEmail)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        val boards = if (query != null) {
            boardRepository.findAllByTitleOrContentContaining(query)
        } else {
            val allBoards = boardRepository.findAll()
            when (boardType) {
                BoardType.FREE, null -> {
                    allBoards
                }

                BoardType.POST -> {
                    allBoards.filter { it.user.id == user.id }
                }

                BoardType.COMMENT -> {
                    allBoards.filter { it.comments.any { it.user.id == user.id } }
                }

                BoardType.SCRAP -> {
                    allBoards.filter {
                        user.id in it.scraps.map { it.user.id }
                    }
                }
            }
        }
        return boards.map { board ->
            BoardSummary(
                board.id!!,
                board.title,
                board.content.take(10),
                Utils.dateTimeToFrontFormat(board.createDatetime),
                board.comments.size
            )
        }.sortedByDescending { it.boardId }
    }

    @Transactional(readOnly = true)
    fun getBoardDetail(token: String, boardId: Long): BoardDetail {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 id에 해당하는 정보가 없습니다.")
        return BoardDetail(
            board.id!!,
            board.title,
            board.content,
            board.likes.size,
            board.comments.size,
            board.scraps.size,
            Utils.dateTimeToFrontFormat(board.createDatetime),
            board.user.id!!,
            board.user.username,
            "",     //TODO
            board.comments.map { comment ->
                BoardDetail.BoardDetailComment(
                    comment.id!!,
                    comment.user.username,
                    comment.content,
                    Utils.dateTimeToFrontFormat(comment.createDatetime)
                )
            }
        )
    }

    @Transactional
    fun createBoard(token: String, request: CreateBoardRequest) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        boardRepository.save(
            Board(
                null,
                user,
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                request.title,
                request.content,
                LocalDateTime.now()
            )
        )
        this.createBoardEvent(user)
    }

    @Transactional
    fun updateBoard(token: String, boardId: Long, request: UpdateBoardRequest) {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        if (board.user.id != user.id) {
            throw Exception("해당 게시물을 수정할 권한이 없습니다.")
        }
        board.update(request.title, request.content)
        boardRepository.save(board)
    }

    @Transactional
    fun deleteBoard(token: String, boardId: Long) {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        if (board.user.id != user.id) {
            throw Exception("해당 게시물을 삭제할 권한이 없습니다.")
        }
        boardRepository.deleteById(board.id!!)
    }

    @Transactional
    fun createComment(token: String, boardId: Long, request: CreateCommentRequest) {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        commentRepository.save(
            Comment(
                null,
                board,
                user,
                request.content,
                LocalDateTime.now()
            )
        )
        this.createCommentEvent(user, board)
    }

    @Transactional
    fun updateComment(token: String, boardId: Long, commentId: Long, request: UpdateCommentRequest) {
        val comment = commentRepository.findById(commentId).getOrNull() ?: throw Exception("댓글 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        if (comment.user.id != user.id) {
            throw Exception("해당 댓글을 수정할 권한이 없습니다.")
        }
        comment.update(request.content)
        commentRepository.save(comment)
    }

    @Transactional
    fun deleteComment(token: String, boardId: Long, commentId: Long) {
        val comment = commentRepository.findById(commentId).getOrNull() ?: throw Exception("댓글 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        if (comment.user.id != user.id) {
            throw Exception("해당 댓글을 삭제할 권한이 없습니다.")
        }
        commentRepository.deleteById(comment.id!!)
    }

    @Transactional
    fun createLikes(token: String, boardId: Long) {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        if (likesRepository.findByUserIdAndBoardId(user.id!!, board.id!!).getOrNull() != null) {
            throw Exception("이미 좋아요를 누른 게시물입니다.")
        }
        likesRepository.save(Likes(null, user, board))
        this.createLikesEvent(user, board)
    }

    @Transactional
    fun deleteLikes(token: String, boardId: Long) {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        likesRepository.deleteByUserIdAndBoardId(user.id!!, board.id!!)
    }

    @Transactional
    fun createScrap(token: String, boardId: Long) {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        if (scrapRepository.findByUserIdAndBoardId(user.id!!, board.id!!).getOrNull() != null) {
            throw Exception("이미 스크랩한 게시물입니다.")
        }
        scrapRepository.save(Scrap(null, user, board))
        this.createScrapEvent(user, board)
    }

    @Transactional
    fun deleteScrap(token: String, boardId: Long) {
        val board = boardRepository.findById(boardId).getOrNull() ?: throw Exception("게시물 정보가 올바르지 않습니다.")
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        scrapRepository.deleteByUserIdAndBoardId(user.id!!, board.id!!)
    }

    private fun createBoardEvent(user: User) {
        when (user.boards.size) {
            1 -> applicationEventPublisher.publishEvent(Post1BoardEvent(user))
            3 -> applicationEventPublisher.publishEvent(Post3BoardEvent(user))
        }
    }

    private fun createCommentEvent(user: User, board: Board) {
        when (user.comments.size) {
            1 -> applicationEventPublisher.publishEvent(Comment1BoardEvent(user))
            5 -> applicationEventPublisher.publishEvent(Comment5BoardEvent(user))
        }
        applicationEventPublisher.publishEvent(CreateBoardCommentEvent(board.user, board.title))
    }

    private fun createLikesEvent(user: User, board: Board) {
        when (user.likes.size) {
            1 -> applicationEventPublisher.publishEvent(Like1BoardEvent(user))
            5 -> applicationEventPublisher.publishEvent(Like5BoardEvent(user))
        }
        applicationEventPublisher.publishEvent(CreateBoardLikesEvent(board.user, board.title))
    }

    private fun createScrapEvent(user: User, board: Board) {
        when (user.scraps.size) {
            1 -> applicationEventPublisher.publishEvent(Scrap1BoardEvent(user))
            5 -> applicationEventPublisher.publishEvent(Scrap5BoardEvent(user))
        }
        applicationEventPublisher.publishEvent(CreateBoardScrapEvent(board.user, board.title))
    }

}