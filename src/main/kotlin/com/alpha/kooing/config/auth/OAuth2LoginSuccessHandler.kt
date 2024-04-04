package com.alpha.kooing.config.auth

import com.alpha.kooing.config.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    val jwtTokenProvider: JwtTokenProvider
):AuthenticationSuccessHandler{
    val authorization_header = "Authorization"
    val bearer_prefix = "Bearer "

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val jwt:String = request.getHeader(authorization_header)
        if(!jwtTokenProvider.validateToken(jwt)) return
        authentication = jwtTokenProvider.
        SecurityContextHolder.getContext().authentication
    }
}