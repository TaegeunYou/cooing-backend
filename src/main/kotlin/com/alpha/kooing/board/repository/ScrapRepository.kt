package com.alpha.kooing.board.repository

import com.alpha.kooing.board.entity.Board
import com.alpha.kooing.board.entity.Comment
import com.alpha.kooing.board.entity.Likes
import com.alpha.kooing.board.entity.Scrap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ScrapRepository: JpaRepository<Scrap, Long> {

    fun findByUserIdAndBoardId(userId: Long, boardId: Long): Optional<Scrap>

    fun deleteByUserIdAndBoardId(userId: Long, boardId: Long)
}