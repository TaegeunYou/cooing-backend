package com.alpha.kooing.user.repository

import com.alpha.kooing.user.entity.ConcernKeyword
import com.alpha.kooing.user.entity.InterestKeyword
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ConcernKeywordRepository : JpaRepository<ConcernKeyword, Long> {
    fun findAllByName(name: String): Optional<ConcernKeyword>
}