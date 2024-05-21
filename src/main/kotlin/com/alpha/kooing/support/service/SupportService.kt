package com.alpha.kooing.support.service

import com.alpha.kooing.support.dto.SupportPolicySummary
import com.alpha.kooing.support.entity.SupportPolicy
import com.alpha.kooing.support.enum.SupportPolicyLocationType
import com.alpha.kooing.support.enum.SupportPolicyType
import com.alpha.kooing.support.repository.JobPostingRepository
import com.alpha.kooing.support.repository.SupportIBusinessRepository
import com.alpha.kooing.support.repository.SupportPolicyRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service


@Service
class SupportService(
    private val supportPolicyRepository: SupportPolicyRepository,
    private val supportIBusinessRepository: SupportIBusinessRepository,
    private val jobPostingRepository: JobPostingRepository,
    private val entityManager: EntityManager,
) {
    fun getSupportPolicy(
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
            SupportPolicySummary(it)
        }
    }
}