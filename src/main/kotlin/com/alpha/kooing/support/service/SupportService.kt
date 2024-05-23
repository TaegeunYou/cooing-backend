package com.alpha.kooing.support.service

import com.alpha.kooing.support.dto.SupportBusinessDetail
import com.alpha.kooing.support.dto.SupportBusinessSummary
import com.alpha.kooing.support.dto.SupportPolicyDetail
import com.alpha.kooing.support.dto.SupportPolicySummary
import com.alpha.kooing.support.entity.SupportBusiness
import com.alpha.kooing.support.entity.SupportPolicy
import com.alpha.kooing.support.enum.SupportBusinessCategoryType
import com.alpha.kooing.support.enum.SupportPolicyLocationType
import com.alpha.kooing.support.enum.SupportPolicyType
import com.alpha.kooing.support.repository.JobPostingRepository
import com.alpha.kooing.support.repository.SupportBusinessRepository
import com.alpha.kooing.support.repository.SupportPolicyRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull


@Service
class SupportService(
    private val supportPolicyRepository: SupportPolicyRepository,
    private val supportBusinessRepository: SupportBusinessRepository,
    private val jobPostingRepository: JobPostingRepository,
    private val entityManager: EntityManager,
) {

    @Transactional(readOnly = true)
    fun getSupportPolicies(
        supportPolicyLocationType: SupportPolicyLocationType?,
        policyType: String?,
        query: String?
    ): List<SupportPolicySummary> {
        val jpql = "select sp from SupportPolicy sp"
        var whereSql = " where true "
        val paramMap = mutableMapOf<String, String>()
        if (supportPolicyLocationType != null) {
            whereSql += "and sp.polyBizSecd = :locationType"
            paramMap["locationType"] = supportPolicyLocationType.name
        }
        if (policyType != null) {
            whereSql += "and sp.polyRlmCd = :supportPolicyType"
            paramMap["supportPolicyType"] = SupportPolicyType.entries.first {
                it.value == policyType
            }.value
        }
        if (query != null) {
            whereSql += "and (sp.polyBizSjnm like CONCAT('%',:keyword,'%') or sp.polyItcnCn like CONCAT('%',:keyword,'%'))"
            paramMap["keyword"] = query
        }
        val sql = entityManager.createQuery(jpql + whereSql, SupportPolicy::class.java)
        paramMap.forEach {
            sql.setParameter(it.key, it.value)
        }
        val resultList = sql.resultList
        return resultList.map {
            SupportPolicySummary(
                it.id!!,
                it.polyBizSjnm,
                it.polyItcnCn
            )
        }
    }

    @Transactional(readOnly = true)
    fun getSupportPolicyDetail(supportPolicyId: Long): SupportPolicyDetail {
        val supportPolicy = supportPolicyRepository.findById(supportPolicyId).getOrNull()
            ?: throw Exception("해당 지원 정책을 찾을 수 없습니다.")
        return SupportPolicyDetail(supportPolicy)
    }

    @Transactional(readOnly = true)
    fun getSupportBusiness(
        supportBusinessCategoryType: SupportBusinessCategoryType?,
        registerYear: String?,
        query: String?
    ): List<SupportBusinessSummary> {
        val jpql = "select sb from SupportBusiness sb"
        var whereSql = " where true "
        val paramMap = mutableMapOf<String, String>()
        if (supportBusinessCategoryType != null && supportBusinessCategoryType != SupportBusinessCategoryType.전체) {
            whereSql += "and sb.category = :category"
            paramMap["category"] = supportBusinessCategoryType.name
        }
        if (registerYear != null) {
            whereSql += "and sb.registerYear = :registerYear"
            paramMap["registerYear"] = registerYear
        }
        if (query != null) {
            whereSql += "and (sb.title like CONCAT('%',:keyword,'%') or sb.content like CONCAT('%',:keyword,'%'))"
            paramMap["keyword"] = query
        }
        val sql = entityManager.createQuery(jpql + whereSql, SupportBusiness::class.java)
        paramMap.forEach {
            sql.setParameter(it.key, it.value)
        }
        val resultList = sql.resultList
        return resultList.map {
            SupportBusinessSummary(
                it.id!!,
                it.title,
                it.category
            )
        }
    }

    @Transactional(readOnly = true)
    fun getSupportBusinessDetail(supportBusinessId: Long): SupportBusinessDetail {
        val supportBusiness = supportBusinessRepository.findById(supportBusinessId).getOrNull()
            ?: throw Exception("해당 지원 사업을 찾을 수 없습니다.")
        return SupportBusinessDetail(
            supportBusiness.title,
            supportBusiness.category,
            supportBusiness.location,
            supportBusiness.recruitPeriod,
            supportBusiness.resultDate,
            supportBusiness.receptionMethod,
            supportBusiness.checkResultMethod,
            supportBusiness.content,
            supportBusiness.organizationName,
            supportBusiness.organizationPerson,
            supportBusiness.organizationContact,
            supportBusiness.filesJson.drop(1).dropLast(1).split(", ").takeIf { it[0].isNotEmpty() }?.map {
                ObjectMapper().readValue(it, SupportBusinessDetail.SupportBusinessSummaryFile::class.java)
            } ?: emptyList(),
            supportBusiness.registerYear
        )
    }
}