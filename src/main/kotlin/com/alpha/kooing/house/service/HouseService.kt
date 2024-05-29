package com.alpha.kooing.house.service

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.house.dto.House
import com.alpha.kooing.house.dto.UpdateHouseRequest
import com.alpha.kooing.user.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class HouseService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun getHouse(token: String): House {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        return if (user.house.isNotEmpty()) {
            ObjectMapper().readValue(user.house, House::class.java)
        } else {
            House(emptyList())
        }
    }

    @Transactional
    fun updateHouse(token: String, request: UpdateHouseRequest) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        user.updateHouse(ObjectMapper().writeValueAsString(request.house))
        userRepository.save(user)
    }
}