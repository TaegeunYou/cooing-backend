package com.alpha.kooing.user.controller

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.dto.UserResponseDto
import com.alpha.kooing.user.entity.User
import com.alpha.kooing.user.service.UserService
import com.alpha.kooing.util.CommonResDto
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class LoginController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
){
    val expiration:Long = 3600 * 60 * 60L

    @PostMapping("/signup")
    fun login(response: HttpServletResponse) : CommonResDto<*>{
        val auth = SecurityContextHolder.getContext().authentication
            ?:return CommonResDto(HttpStatus.BAD_REQUEST, "authentication not exists", null)
        val oauthUser = auth.principal as CustomOAuth2User
        val user = userService.save(User(
            email = oauthUser.email,
            username = oauthUser.username,
            role = Role.USER,
        ))?:return CommonResDto(HttpStatus.BAD_REQUEST, "BAD REQUEST", null)
        val newToken = jwtTokenProvider.createJwt(
            id = user.id,
            email = user.email,
            role = Role.USER.name,
            expiration = expiration
        )
        response.addCookie(Cookie("Authorization", newToken))
        return CommonResDto(HttpStatus.OK, "OK", user)
    }
}