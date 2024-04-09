package com.alpha.kooing.config.auth

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.dto.CustomOAuth2User
import io.jsonwebtoken.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomOAuth2SuccessHandler(
    val jwtTokenProvider: JwtTokenProvider
):SimpleUrlAuthenticationSuccessHandler(){
    private val jwtTokenExpiration = 60 * 60 * 60L
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val customOAuth2User = authentication.principal as CustomOAuth2User
        val email = customOAuth2User.email
        val authorities = authentication.authorities
        val role = authorities.iterator().next().authority
        val token = jwtTokenProvider.createJwt(email=email, role=role, expiration = jwtTokenExpiration)
        println("success")
        response.addCookie(createCookie("Authorization", token))
        response.sendRedirect("http://localhost:3000/")
    }

    fun createCookie(key:String, value:String):Cookie{
        val cookie = Cookie(key, value)
        // 쿠키 수명 설정
        cookie.maxAge = jwtTokenExpiration.toInt()
        // 쿠키 유효 경로 : "/" => 모든 경로
        cookie.path = "/"
        // js가 쿠키를 가져가지 못하도록 함
        cookie.isHttpOnly = true
        return cookie
    }
}