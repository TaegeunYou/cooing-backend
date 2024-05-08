package com.alpha.kooing.board.controller

import com.alpha.kooing.board.dto.*
import com.alpha.kooing.board.service.CollegeService
import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CollegeController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val collegeService: CollegeService
) {

    @GetMapping("/clubs")
    fun getClubs(
        httpServletRequest: HttpServletRequest,
        @RequestParam("query") query: String?,
    ): ResponseEntity<ApiResponse<List<ClubSummary>>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                collegeService.getClubs(token, query)
            )
        )
    }

    @GetMapping("/club/{clubId}")
    fun getClub(
        httpServletRequest: HttpServletRequest,
        @PathVariable("clubId") clubId: Long,
    ): ResponseEntity<ApiResponse<ClubDetail>> {
        return ResponseEntity.ok(
            ApiResponse.success(
                collegeService.getClub(clubId)
            )
        )
    }

    @PostMapping("/club")
    fun createClub(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: CreateClubRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        collegeService.createClub(token, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/studies")
    fun getStudies(
        httpServletRequest: HttpServletRequest,
        @RequestParam("query") query: String?,
    ): ResponseEntity<ApiResponse<List<StudySummary>>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok(
            ApiResponse.success(
                collegeService.getStudies(token, query)
            )
        )
    }

    @GetMapping("/study/{studyId}")
    fun getStudy(
        httpServletRequest: HttpServletRequest,
        @PathVariable("studyId") studyId: Long,
    ): ResponseEntity<ApiResponse<StudyDetail>> {
        return ResponseEntity.ok(
            ApiResponse.success(
                collegeService.getStudy(studyId)
            )
        )
    }

    @PostMapping("/study")
    fun createStudy(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: CreateStudyRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        collegeService.createStudy(token, request)
        return ResponseEntity.ok().build()
    }
}