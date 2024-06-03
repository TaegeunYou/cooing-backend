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
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.Base64
import java.util.Objects
import javax.print.attribute.standard.Media
import kotlin.math.exp
import kotlin.math.log

@RestController
class LoginController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
    private val userManager: LoginUserManager,
    private val loginService: LoginService
){
    val expiration:Long = 3600 * 60 * 60L

    @GetMapping("/test-user")
    fun getTestUser():ApiResponse<*>{
        val users = userService.findAll()?:return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        val user = users.getOrNull(0)?:return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        val testUserToken = jwtTokenProvider.createJwt(
            id = user.id,
            email = user.email,
            username = user.username,
            role = user.role.name,
            expiration = jwtTokenProvider.expiration
        )
        users.map {
            val token = jwtTokenProvider.createJwt(
                id = it.id,
                email = it.email,
                username = it.username,
                role = it.role.name,
                expiration = jwtTokenProvider.expiration
            )
            userManager.loginUser(it.id, token)
        }
        userManager.loginUser(user.id, testUserToken)
        return ApiResponse(
            message = HttpStatus.OK.name,
            body = testUserToken
        )
    }

    @PostMapping("/sign-in")
    fun signIn(response: HttpServletResponse, @RequestBody loginDto:UserLoginDto): ApiResponse<*>{
        try {
            val token = loginDto.token
            val userJwtToken = loginService.getLoginToken(token)?:throw Exception()
            response.setHeader("Authorization", userJwtToken)
            return ApiResponse("generate token success", userJwtToken)
        }catch (e:Exception) {
            println(e.printStackTrace())
            return ApiResponse("invalid token", null)
        }
    }


    @PostMapping("/signup", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signup(request: HttpServletRequest, response: HttpServletResponse, @RequestPart userInfo:UserCreateDto, @RequestPart("profileImage", required = false) profileImage: MultipartFile?) : ApiResponse<*>{
        try {
            val token = jwtTokenProvider.resolveToken(request)
            val user = userService.saveUser(token, userInfo, profileImage)?:return ApiResponse("save user fail", null)
            val newToken = jwtTokenProvider.createJwt(
                id = user.id,
                email = user.email,
                username = user.username,
                role = Role.USER.name,
                expiration = expiration
            )
            userManager.loginUser(user.id, newToken)
            return ApiResponse(HttpStatus.OK.name, newToken)
        }catch (e:Exception){
            e.printStackTrace()
            return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        }
    }

    @GetMapping("/signout")
    fun logout(request: HttpServletRequest):ApiResponse<*>{
        try {
            val token = jwtTokenProvider.resolveToken(request)
            val userId = jwtTokenProvider.getJwtUserId(token).toLong()
            userManager.logoutUser(userId)
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