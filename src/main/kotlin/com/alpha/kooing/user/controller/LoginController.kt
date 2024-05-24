package com.alpha.kooing.user.controller

import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.LoginUserManager
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.User
import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.dto.GoogleUserDto
import com.alpha.kooing.user.dto.UserCreateDto
import com.alpha.kooing.user.dto.UserLoginDto
import com.alpha.kooing.user.enum.RoleType
import com.alpha.kooing.user.service.LoginService
import com.alpha.kooing.user.service.UserService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.openid.connect.sdk.claims.UserInfo
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.json.JsonParser
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.Base64
import java.util.Objects
import kotlin.math.log

@RestController
class LoginController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
    private val userManager: LoginUserManager,
    private val loginService: LoginService
){
    val expiration:Long = 3600 * 60 * 60L

    @PostMapping("/sign-in")
    fun signIn(response: HttpServletResponse, @RequestBody loginDto:UserLoginDto): ApiResponse<*>{
        try {
            val token = loginDto.token
            val tokenInfo = loginService.getLoginToken(token)?:throw Exception()
            val userJwtToken = tokenInfo.getOrDefault("token", null)?:throw Exception()
            val userRole = tokenInfo.getOrDefault("role", null)?:throw Exception()
            response.setHeader("Set-Cookie", loginService.createCookieWithToken(userJwtToken).toString())
            return ApiResponse("generate token success", userRole)
        }catch (e:Exception) {
            return ApiResponse("invalid token", Role.GUEST.name)
        }
    }


    @PostMapping("/signup")
    fun signup(request: HttpServletRequest, response: HttpServletResponse, @RequestBody userInfo:UserCreateDto) : ApiResponse<*>{
        try {
//            val token = jwtTokenProvider.resolveToken(request)
            val token = jwtTokenProvider.createRandomToken()
            val user = userService.saveUser(token, userInfo)?:return ApiResponse("save user fail", null)
            val newToken = jwtTokenProvider.createJwt(
                id = user.id,
                email = user.email,
                username = user.username,
                role = Role.USER.name,
                expiration = expiration
            )
            userManager.loginUser(user.id, newToken)
            response.addCookie(Cookie("Authorization", newToken))
            return ApiResponse(HttpStatus.OK.name, user.toUserDetail())
        }catch (e:Exception){
            e.printStackTrace()
            return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        }
    }

    @GetMapping("/signout")
    fun logout():ApiResponse<*>{
        try {
            val oauthUser = SecurityContextHolder.getContext().authentication.principal as CustomOAuth2User
            userManager.logoutUser(oauthUser.id)
            return ApiResponse(HttpStatus.OK.name, null)
        }catch (e:Exception){
            return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        }
    }

    @GetMapping("/login-info")
    @Operation(summary="현재 사용자 로그인 여부 조회")
    fun getLoginInfo(servletRequest: HttpServletRequest, response: HttpServletResponse): ApiResponse<*>{
        try {
            val token = jwtTokenProvider.resolveToken(servletRequest)
            println(jwtTokenProvider.getJwtEmail(token))
            return ApiResponse(HttpStatus.OK.name, jwtTokenProvider.getJwtRole(token))
        }catch (e:Exception){
            return ApiResponse(HttpStatus.OK.name, Role.GUEST.name)
        }
    }
}