package com.alpha.kooing.board.repository

import com.alpha.kooing.board.entity.Club
import com.alpha.kooing.board.entity.Study
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StudyRepository: JpaRepository<Study, Long> {
    @Query("select s from Study s where s.title like CONCAT('%',:keyword,'%') or s.content like CONCAT('%',:keyword,'%')")
    fun findAllByTitleOrContentContaining(@Param("keyword") keyword: String): List<Study>
}