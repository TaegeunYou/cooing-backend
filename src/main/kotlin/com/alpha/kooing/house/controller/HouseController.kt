package com.alpha.kooing.house.controller

import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.house.dto.GetHouseResponse
import com.alpha.kooing.house.service.HouseService
import com.alpha.kooing.house.dto.House
import com.alpha.kooing.house.dto.UpdateHouseRequest
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HouseController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val houseService: HouseService,
) {

    @GetMapping("/house")
    @Operation(summary = "과자집 조회")
    fun getHouse(
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<GetHouseResponse>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                GetHouseResponse(
                    houseService.getHouse(token)
                )
            )
        )
    }

    @PostMapping("/house")
    @Operation(summary = "과자집 업데이트")
    fun updateHouse(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: UpdateHouseRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        houseService.updateHouse(token, request)
        return ResponseEntity.ok().build()
    }
}