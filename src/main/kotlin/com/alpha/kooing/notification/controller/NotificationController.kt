package com.alpha.kooing.notification.controller

import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.notification.dto.GetNotificationsResponse
import com.alpha.kooing.notification.service.NotificationService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val notificationService: NotificationService
) {

    @GetMapping("/notifications")
    @Operation(summary = "알림 목록 조회")
    fun getNotifications(
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<GetNotificationsResponse>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                notificationService.getNotifications(token)
            )
        )
    }
}