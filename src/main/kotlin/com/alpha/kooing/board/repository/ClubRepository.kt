package com.alpha.kooing.board.repository

import com.alpha.kooing.board.entity.Board
import com.alpha.kooing.board.entity.Club
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ClubRepository: JpaRepository<Club, Long> {

    @Query("select c from Club c where c.title like CONCAT('%',:keyword,'%') or c.summary like CONCAT('%',:keyword,'%') or c.content like CONCAT('%',:keyword,'%')")
    fun findAllByTitleOrSummaryOrContentContaining(@Param("keyword") keyword: String): List<Club>

}