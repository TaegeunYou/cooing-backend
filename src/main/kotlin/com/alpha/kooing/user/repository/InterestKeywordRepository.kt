package com.alpha.kooing.user.repository

import com.alpha.kooing.user.entity.InterestKeyword
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface InterestKeywordRepository : JpaRepository<InterestKeyword, Long> {
    fun findAllByName(name: String): Optional<InterestKeyword>
}