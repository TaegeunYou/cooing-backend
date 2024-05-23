package com.alpha.kooing.support.controller

import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.support.dto.SupportBusinessDetail
import com.alpha.kooing.support.dto.SupportBusinessSummary
import com.alpha.kooing.support.dto.SupportPolicyDetail
import com.alpha.kooing.support.dto.SupportPolicySummary
import com.alpha.kooing.support.enum.SupportBusinessCategoryType
import com.alpha.kooing.support.enum.SupportPolicyLocationType
import com.alpha.kooing.support.service.SupportBusinessSchedulingService
import com.alpha.kooing.support.service.SupportPolicySchedulingService
import com.alpha.kooing.support.service.SupportService
import io.swagger.v3.oas.annotations.Operation
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
) {

    @GetMapping("/support/policy")
    @Operation(summary = "지원 정책 목록 조회")
    fun getSupportPolicies(
        @RequestParam("polyBizSecd") supportPolicyLocationType: SupportPolicyLocationType?,
        @RequestParam("polyRlmCd") policyType: String?,
        @RequestParam("query") query: String?,
    ): ResponseEntity<ApiResponse<List<SupportPolicySummary>>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getSupportPolicies(supportPolicyLocationType, policyType, query)
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
    ): ResponseEntity<ApiResponse<List<SupportBusinessSummary>>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getSupportBusiness(supportBusinessCategoryType, registerYear, query)
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

//    @GetMapping
//    fun getJobPosting(
//        httpServletRequest: HttpServletRequest,
//        @RequestParam("locationType") locationType: LocationType?,
//        @RequestParam("policyType") jobType: JobType?,
//        @RequestParam("query") query: String?,
//    ): ResponseEntity<ApiResponse<List<JobPostingSummary>>> {
//        return ResponseEntity.ok().body(
//            ApiResponse.success(
//                supportService.getSupportInfo(locationType, policyType, query)
//            )
//        )
//    }
}