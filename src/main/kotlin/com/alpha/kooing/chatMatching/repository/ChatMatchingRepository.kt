package com.alpha.kooing.chatMatching.repository

import com.alpha.kooing.chatMatching.entity.ChatMatching
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMatchingRepository:JpaRepository<ChatMatching, Long>