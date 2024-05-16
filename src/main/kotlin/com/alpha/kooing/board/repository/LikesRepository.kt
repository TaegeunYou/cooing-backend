package com.alpha.kooing.board.repository

import com.alpha.kooing.board.entity.Likes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LikesRepository: JpaRepository<Likes, Long> {
    fun findByUserIdAndBoardId(userId: Long, boardId: Long): Optional<Likes>
    fun deleteByUserIdAndBoardId(userId: Long, boardId: Long)
}