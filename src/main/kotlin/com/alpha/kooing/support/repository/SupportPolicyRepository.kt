package com.alpha.kooing.support.repository

import com.alpha.kooing.board.entity.Board
import com.alpha.kooing.support.entity.SupportPolicy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SupportPolicyRepository: JpaRepository<SupportPolicy, Long> {
    @Query("select sp.bizId from SupportPolicy sp")
    fun getAllBizIds(): List<String?>
}