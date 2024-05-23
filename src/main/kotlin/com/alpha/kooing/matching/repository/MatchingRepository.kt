package com.alpha.kooing.matching.repository

import com.alpha.kooing.matching.entity.Matching
import org.springframework.data.jpa.repository.JpaRepository

interface MatchingRepository:JpaRepository<Matching, Long>