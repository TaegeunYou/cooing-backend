package com.alpha.kooing.support.controller

import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.support.dto.*
import com.alpha.kooing.support.enum.SupportBusinessCategoryType
import com.alpha.kooing.support.enum.SupportLocationType
import com.alpha.kooing.support.service.JobPostingSchedulingService
import com.alpha.kooing.support.service.SupportBusinessSchedulingService
import com.alpha.kooing.support.service.SupportPolicySchedulingService
import com.alpha.kooing.support.service.SupportService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SupportController(
    private val supportService: SupportService,
    private val supportPolicySchedulingService: SupportPolicySchedulingService,
    private val supportBusinessSchedulingService: SupportBusinessSchedulingService,
    private val jobPostingSchedulingService: JobPostingSchedulingService,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @GetMapping("/support/policy")
    @Operation(summary = "지원 정책 목록 조회")
    fun getSupportPolicies(
        @RequestParam("supportLocationType") supportLocationType: SupportLocationType?,
        @RequestParam("polyRlmCd") policyType: String?,
        @RequestParam("query") query: String?,
        @RequestParam("scrap") scrap: Boolean?,
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<List<SupportPolicySummary>>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getSupportPolicies(token, supportLocationType, policyType, query, scrap)
            )
        )
    }


    @GetMapping("/support/policy/{id}")
    @Operation(summary = "지원 정책 상세 조회")
    fun getSupportPolicyDetail(
        @PathVariable("id") supportPolicyId: Long,
    ): ResponseEntity<ApiResponse<SupportPolicyDetail>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getSupportPolicyDetail(supportPolicyId)
            )
        )
    }

    @GetMapping("/support/business")
    @Operation(summary = "지원 사업 목록 조회")
    fun getSupportBusiness(
        @RequestParam("category") supportBusinessCategoryType: SupportBusinessCategoryType?,
        @RequestParam("registerYear") registerYear: String?,
        @RequestParam("query") query: String?,
        @RequestParam("scrap") scrap: Boolean?,
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<List<SupportBusinessSummary>>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getSupportBusiness(token, supportBusinessCategoryType, registerYear, query, scrap)
            )
        )
    }

    @GetMapping("/support/business/{id}")
    @Operation(summary = "지원 사업 상세 조회")
    fun getSupportBusinessDetail(
        @PathVariable("id") supportBusinessId: Long,
    ): ResponseEntity<ApiResponse<SupportBusinessDetail>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getSupportBusinessDetail(supportBusinessId)
            )
        )
    }

    @GetMapping("/support/business/{id}/file")
    fun getSupportBusinessDetailFile(
        @PathVariable("id") supportBusinessId: Long,
    ): ResponseEntity<ApiResponse<SupportBusinessDetail>> {
        TODO()
    }

    @GetMapping("/support/job")
    @Operation(summary = "지원 정책 목록 조회")
    fun getJobPostings(
        @RequestParam("supportLocationType") supportLocationType: SupportLocationType?,
        @RequestParam("ncsCdNmLst") jobType: String?,
        @RequestParam("query") query: String?,
        @RequestParam("scrap") scrap: Boolean?,
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<List<JobPostingSummary>>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getJobPostings(token, supportLocationType, jobType, query, scrap)
            )
        )
    }

    @GetMapping("/support/job/{id}")
    @Operation(summary = "지원 정책 상세 조회")
    fun getJobPostingDetail(
        @PathVariable("id") jobPostingId: Long,
    ): ResponseEntity<ApiResponse<JobPostingDetail>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getJobPostingDetail(jobPostingId)
            )
        )
    }

    @PostMapping("/support/policy/{id}/scrap")
    @Operation(summary = "지원 정책 스크랩")
    fun scrapSupportPolicy(
        @PathVariable("id") supportPolicyId: Long,
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        supportService.scrapSupportPolicy(token, supportPolicyId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/support/business/{id}/scrap")
    @Operation(summary = "지원 사업 스크랩")
    fun scrapSupportBusiness(
        @PathVariable("id") supportBusinessId: Long,
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        supportService.scrapSupportBusiness(token, supportBusinessId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/support/job/{id}/scrap")
    @Operation(summary = "채용 공고 스크랩")
    fun scrapJobPosting(
        @PathVariable("id") jobPostingId: Long,
        httpServletRequest: HttpServletRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = jwtTokenProvider.resolveToken(httpServletRequest)
        supportService.scrapJobPosting(token, jobPostingId)
        return ResponseEntity.ok().build()
    }


    @PostMapping("/support/policy")
    @Operation(summary = "지원 정책 데이터 업데이트 (프론트 연동 x)")
    fun updateSupportPolicy() {
        supportPolicySchedulingService.updateSupportPolicy()
    }

    @PostMapping("/support/business")
    @Operation(summary = "지원 사업 데이터 업데이트 (프론트 연동 x)")
    fun updateSupportBusiness() {
        supportBusinessSchedulingService.updateSupportBusiness()
    }

    @PostMapping("/support/job")
    @Operation(summary = "채용 공고 데이터 업데이트 (프론트 연동 x)")
    fun updateSupportJob() {
        jobPostingSchedulingService.updateJobPosting()
    }
}