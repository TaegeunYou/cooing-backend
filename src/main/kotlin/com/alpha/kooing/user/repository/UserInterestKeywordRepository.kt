package com.alpha.kooing.user.repository

import com.alpha.kooing.user.entity.UserInterestKeyword
import org.springframework.data.jpa.repository.JpaRepository

interface UserInterestKeywordRepository : JpaRepository<UserInterestKeyword, Long>