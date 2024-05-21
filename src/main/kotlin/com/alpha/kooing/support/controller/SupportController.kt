package com.alpha.kooing.support.controller

import com.alpha.kooing.common.dto.ApiResponse
import com.alpha.kooing.support.dto.SupportBusinessSummary
import com.alpha.kooing.support.dto.SupportPolicySummary
import com.alpha.kooing.support.enum.SupportPolicyLocationType
import com.alpha.kooing.support.service.SupportPolicySchedulingService
import com.alpha.kooing.support.service.SupportService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SupportController(
    private val supportService: SupportService,
    private val supportPolicySchedulingService: SupportPolicySchedulingService
) {

    @PostMapping("/support/policy")
    fun schedulingTest() {
        supportPolicySchedulingService.getSupportPolicy()
    }

    @GetMapping("/support/policy")
    fun getSupportPolicy(
        httpServletRequest: HttpServletRequest,
        @RequestParam("polyBizSecd") supportPolicyLocationType: SupportPolicyLocationType?,
        @RequestParam("polyRlmCd") policyType: String?,
        @RequestParam("query") query: String?,
    ): ResponseEntity<ApiResponse<List<SupportPolicySummary>>> {
        return ResponseEntity.ok().body(
            ApiResponse.success(
                supportService.getSupportPolicy(supportPolicyLocationType, policyType, query)
            )
        )
    }

//    @GetMapping
//    fun getSupportBusiness(
//        httpServletRequest: HttpServletRequest,
//        @RequestParam("locationType") locationType: LocationType?,
//        @RequestParam("policyType") infoType: SupportInfoType?,
//        @RequestParam("query") query: String?,
//    ): ResponseEntity<ApiResponse<List<SupportBusinessSummary>>> {
//        return ResponseEntity.ok().body(
//            ApiResponse.success(
//                supportService.getSupportInfo(locationType, policyType, query)
//            )
//        )
//    }
//
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