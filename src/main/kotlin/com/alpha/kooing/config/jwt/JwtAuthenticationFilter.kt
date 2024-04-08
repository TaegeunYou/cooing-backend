package com.alpha.kooing.config.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    val jwtTokenProvider: JwtTokenProvider
):OncePerRequestFilter(){
    private val authorizationHeader = "Authorization"
    private val bearerPrefix = "bearer "
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorization = getJwtToken(request)
        if(authorization!=null && jwtTokenProvider.validateToken(authorization)){
            val principal = jwtTokenProvider.getAuthentication(authorization)
            SecurityContextHolder.getContext().authentication = principal
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwtToken(request: HttpServletRequest):String?{
        val bearerToken = request.getHeader(authorizationHeader)
        if(bearerToken.startsWith(bearerPrefix) && StringUtils.hasText(bearerToken)){
            return bearerToken.substring(7)
        }
        return null
    }
}