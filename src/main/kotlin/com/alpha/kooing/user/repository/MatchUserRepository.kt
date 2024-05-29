package com.alpha.kooing.user.repository

import com.alpha.kooing.user.entity.MatchUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MatchUserRepository:JpaRepository<MatchUser, Long> {
    @Query("DELETE FROM MatchUser u WHERE u.user.id=:userId")
    fun deleteAllByUserId(userId:Long)

    @Query("SELECT u FROM MatchUser u WHERE u.user.id=:userId")
    fun findAllByUserId(userId:Long):List<MatchUser>
}