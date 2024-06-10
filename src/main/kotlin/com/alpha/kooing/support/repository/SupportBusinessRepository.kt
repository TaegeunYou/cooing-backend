package com.alpha.kooing.support.repository

import com.alpha.kooing.support.entity.SupportBusiness
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SupportBusinessRepository: JpaRepository<SupportBusiness, Long> {
    @Query("select sb.idx from SupportBusiness sb")
    fun getAllIdx(): List<String?>
}