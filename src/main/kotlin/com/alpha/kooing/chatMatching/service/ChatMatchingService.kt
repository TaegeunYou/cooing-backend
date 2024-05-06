package com.alpha.kooing.chatMatching.service

import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.chatMatching.repository.ChatMatchingRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatMatchingService(
    val chatMatchingRepository: ChatMatchingRepository
){
    @Transactional
    fun save(chatMatching: ChatMatching):ChatMatching{
        return chatMatchingRepository.save(chatMatching)
    }
}