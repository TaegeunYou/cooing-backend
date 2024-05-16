package com.alpha.kooing.user.controller

import com.alpha.kooing.config.LoginUserManager
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.User
import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.enum.RoleType
import com.alpha.kooing.user.service.UserService
import com.alpha.kooing.util.CommonResDto
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
    private val userManager: LoginUserManager
){
    val expiration:Long = 3600 * 60 * 60L

    @PostMapping("/signup")
    fun signup(response: HttpServletResponse) : CommonResDto<*>{
        val auth = SecurityContextHolder.getContext().authentication
            ?:return CommonResDto(HttpStatus.BAD_REQUEST, "authentication not exists", null)
        val oauthUser = auth.principal as CustomOAuth2User
        println(oauthUser.email)
        val user = userService.save(
            User(
                email = oauthUser.email,
                username = oauthUser.username,
                role = Role.USER,
                isMatchingActive = false,
                profileImageUrl = "",
                profileMessage = "",
                roleType = RoleType.그레텔
            )
        )?:return CommonResDto(HttpStatus.BAD_REQUEST, "BAD REQUEST", null)
        val newToken = jwtTokenProvider.createJwt(
            id = user.id,
            email = user.email,
            role = Role.USER.name,
            expiration = expiration
        )
        userManager.loginUser(user.id, newToken)
        response.addCookie(Cookie("Authorization", newToken))
        return CommonResDto(HttpStatus.OK, "OK", user)
    }

    @GetMapping("/signout")
    fun logout():CommonResDto<*>{
        try {
            val oauthUser = SecurityContextHolder.getContext().authentication.principal as CustomOAuth2User
            userManager.logoutUser(oauthUser.id)
            return CommonResDto(HttpStatus.OK, "Logout Success", null)
        }catch (e:Exception){
            return CommonResDto(HttpStatus.BAD_REQUEST, "bad request", null)
        }
    }
}