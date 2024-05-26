package com.alpha.kooing.user.service

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.GoogleUserDto
import com.alpha.kooing.user.repository.UserRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
){

    fun createCookieWithToken(token:String): ResponseCookie {
        val cookie = ResponseCookie
            .from("Authorization", token)
            .domain("localhost")
            .path("/")
            .maxAge(jwtTokenProvider.expiration)
            .httpOnly(true)
            .sameSite("None")
            .build()
        return cookie
    }

    fun resolveTokenToObject(token:String):GoogleUserDto?{
        try {
            val payload = token.split(".")[1]
            println(payload)
            val decoder = Base64.getUrlDecoder()
            val objectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            val userInfoJsonStr = String(decoder.decode(payload))
            val userInfoJson = objectMapper.readValue(userInfoJsonStr, JsonNode::class.java)
            return GoogleUserDto(
                email = userInfoJson.get("email").asText(),
                name = userInfoJson.get("name").asText(),
            )
        }catch (e:Exception){
            return null
        }
    }

    fun getLoginToken(token:String):MutableMap<String, String>?{
        val userInfo = resolveTokenToObject(token)?:return null
        val existsUser = userRepository.findByEmail(userInfo.email).orElse(null)
        val res = mutableMapOf<String,String>()
        if(existsUser != null) {
            res["token"] = jwtTokenProvider.createJwt(
                id = existsUser.id,
                email = existsUser.email,
                username = existsUser.username,
                role = Role.USER.name,
                expiration = jwtTokenProvider.expiration
            )
            res["role"] = Role.USER.name
        }else{
            res["token"] =  jwtTokenProvider.createJwt(
                id = -1,
                email = userInfo.email,
                username = userInfo.name,
                role = Role.LIMITED.name,
                expiration = jwtTokenProvider.expiration
            )
            res["role"] = Role.LIMITED.name
        }
        return res
    }
}