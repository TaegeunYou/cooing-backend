package com.alpha.kooing.user.service

import com.alpha.kooing.config.LoginUserManager
import com.alpha.kooing.user.dto.UserResponseDto
import com.alpha.kooing.user.entity.User
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userManager: LoginUserManager
){
    @Transactional
    fun findAll(): List<UserResponseDto>?{
        val result = userRepository.findAll()
        if(result.isEmpty()){
            return null
        }
        return result.map { it.toResponseDto() }
    }

    @Transactional
    fun save(user: User): UserResponseDto? {
        try {
            if(userRepository.findByEmail(user.email).orElse(null)!=null){
                return null
            }
            val result = userRepository.save(user)
            println("user : ${user.toString()}")
            val resDto = result.toResponseDto()
            println("resDto : ${resDto.toString()}")
            return resDto
        }catch (e: Exception){
            e.printStackTrace()
            return null
        }
    }

    @Transactional
    fun findMatchingUser(): List<UserResponseDto>?{
        val users = userManager.getLoginUserList()
        return users
    }
}