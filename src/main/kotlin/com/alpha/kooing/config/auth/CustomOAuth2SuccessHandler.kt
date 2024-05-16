package com.alpha.kooing.config.auth

import com.alpha.kooing.config.LoginUserManager
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.entity.User
import io.jsonwebtoken.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.stereotype.Component

@Component
class CustomOAuth2SuccessHandler(
    val jwtTokenProvider: JwtTokenProvider,
    val userManager: LoginUserManager
):SimpleUrlAuthenticationSuccessHandler(){
    private val jwtTokenExpiration = 3600 * 60 * 60L
    @Throws(IOException::class, ServletException::class)
    // filterchain 없는 놈으로 해야함. 아니면 successhandler로 안넘어감
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val user = authentication.principal as CustomOAuth2User
        val email = user.email
        val userId = user.id
        val role = user.role.name
        val token = jwtTokenProvider.createJwt(email=email, role=role, id = userId, expiration = jwtTokenExpiration)
        if(role == Role.USER.name){
            userManager.loginUser(userId, token)
        }
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