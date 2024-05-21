package com.alpha.kooing.user.controller

import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.dto.*
import com.alpha.kooing.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@RestController
class UserController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
){
    @GetMapping("/user/google/login")
    fun googleLogin(){
        val restTemplate = RestTemplate()
    }

    @GetMapping("/user")
    @Operation(summary = "유저 정보 조회")
    fun getUser(
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<UserDetail>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                userService.getUser(token)
            )
        )
    }

    @PutMapping("/user/profile")
    @Operation(summary = "유저 프로필 수정")
    fun updateUserProfile(
        httpServletRequest: HttpServletRequest,
        @RequestPart("request") request: UpdateUserProfileRequest,
        @RequestPart("profileImage") profileImage: MultipartFile?
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        userService.updateUserProfile(token, request, profileImage)
        return ResponseEntity.ok().build()
    }


    @PutMapping("/user/status")
    @Operation(summary = "유저 매칭 기능 활성화/비활성화")
    fun updateUserMatchingStatus(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: UpdateUserMatchingStatusRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        userService.updateUserMatchingStatus(token, request)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/user/keyword")
    @Operation(summary = "유저 매칭 정보 수정")
    fun updateUserMatchingKeyword(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: UpdateUserMatchingKeywordRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        userService.updateUserMatchingKeyword(token, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/user/checklist")
    @Operation(summary = "자립 체크 리스트 조회")
    fun getUserCheckList(
    ): ResponseEntity<ApiResponse<GetUserCheckListResponse>> {
        return ResponseEntity.ok(
            ApiResponse.success(
                userService.getUserCheckList()
            )
        )
    }
}