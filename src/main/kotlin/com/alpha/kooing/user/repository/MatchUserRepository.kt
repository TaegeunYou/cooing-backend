package com.alpha.kooing.user.repository

import com.alpha.kooing.user.entity.MatchUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface MatchUserRepository:JpaRepository<MatchUser, Long> {
    @Modifying
    @Query("DELETE FROM MatchUser u WHERE u.user.id=:userId")
    fun deleteAllByUserId(userId:Long)

    @Query("SELECT u FROM MatchUser u WHERE u.user.id=:userId or u.matchUser.id=:userId")
    fun findByUserId(userId:Long): Optional<MatchUser>
}