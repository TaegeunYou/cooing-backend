package com.alpha.kooing.userMatching.repository

import com.alpha.kooing.userMatching.entity.UserMatching
import org.springframework.data.jpa.repository.JpaRepository

interface UserMatchingRepository: JpaRepository<UserMatching, Long>