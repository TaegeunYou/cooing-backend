package com.alpha.kooing.user.service

import com.alpha.kooing.user.dto.UserResponseDto
import com.alpha.kooing.user.entity.User
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
){
    @Transactional
    fun findAll(): List<UserResponseDto>?{
        val result = userRepository.findAll()
        if(result.isEmpty()){
            return null
        }
        return result.map { it.toResponseDto() }
    }
}