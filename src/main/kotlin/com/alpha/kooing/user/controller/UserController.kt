package com.alpha.kooing.user.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.dto.*
import com.alpha.kooing.user.service.UserService
import com.alpha.kooing.user.dto.UpdateUserMatchingKeywordRequest
import com.alpha.kooing.user.dto.UpdateUserMatchingStatusRequest
import com.alpha.kooing.user.dto.UpdateUserProfileRequest
import com.alpha.kooing.user.dto.UserDetail
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class UserController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
){
    @GetMapping("/users")
    fun findAllUsers() : ApiResponse<*>{
        val result = userService.findAll() ?: return ApiResponse(HttpStatus.BAD_REQUEST.name, null)
        return ApiResponse(HttpStatus.OK.name, result)
    }

    @GetMapping("/user/match")
    fun findSimilarInterest(@RequestParam hobbies : List<String>) : ApiResponse<*>{
        val users = userService.findMatchingUser()
        return ApiResponse(HttpStatus.OK.name, users)
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