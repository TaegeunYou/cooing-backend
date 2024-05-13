package com.alpha.kooing.board.repository

import com.alpha.kooing.board.entity.Board
import com.alpha.kooing.board.entity.Club
import com.alpha.kooing.board.entity.Volunteer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VolunteerRepository: JpaRepository<Volunteer, Long> {

    @Query("select v from Volunteer v where v.title like CONCAT('%',:keyword,'%') or v.summary like CONCAT('%',:keyword,'%') or v.content like CONCAT('%',:keyword,'%')")
    fun findAllByTitleOrSummaryOrContentContaining(@Param("keyword") keyword: String): List<Volunteer>

}