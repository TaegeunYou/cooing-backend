package com.alpha.kooing.board.repository

import com.alpha.kooing.board.entity.Board
import com.alpha.kooing.board.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long>