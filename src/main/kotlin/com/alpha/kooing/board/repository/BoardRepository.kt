package com.alpha.kooing.board.repository

import com.alpha.kooing.board.entity.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository: JpaRepository<Board, Long> {
    @Query("select b from Board b where b.title like CONCAT('%',:keyword,'%') or b.content like CONCAT('%',:keyword,'%')")
    fun findAllByTitleOrContentContaining(@Param("keyword") keyword: String): List<Board>

}
