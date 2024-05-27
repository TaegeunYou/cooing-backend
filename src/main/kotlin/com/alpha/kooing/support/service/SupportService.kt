package com.alpha.kooing.support.service

import com.alpha.kooing.support.dto.*
import com.alpha.kooing.support.entity.JobPosting
import com.alpha.kooing.support.entity.SupportBusiness
import com.alpha.kooing.support.entity.SupportPolicy
import com.alpha.kooing.support.enum.SupportBusinessCategoryType
import com.alpha.kooing.support.enum.SupportLocationType
import com.alpha.kooing.support.enum.SupportPolicyType
import com.alpha.kooing.support.repository.JobPostingRepository
import com.alpha.kooing.support.repository.SupportBusinessRepository
import com.alpha.kooing.support.repository.SupportPolicyRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import javax.imageio.ImageIO
import kotlin.jvm.optionals.getOrNull


@Service
class SupportService(
    private val supportPolicyRepository: SupportPolicyRepository,
    private val supportBusinessRepository: SupportBusinessRepository,
    private val jobPostingRepository: JobPostingRepository,
    private val entityManager: EntityManager,
    private val restTemplate: RestTemplate,
) {

    @Transactional(readOnly = true)
    fun getSupportPolicies(
        supportLocationType: SupportLocationType?,
        policyType: String?,
        query: String?
    ): List<SupportPolicySummary> {
        val jpql = "select sp from SupportPolicy sp"
        var whereSql = " where true"
        val paramMap = mutableMapOf<String, String>()
        if (supportLocationType != null) {
            whereSql += " and sp.polyBizSecd = :locationType"
            paramMap["locationType"] = supportLocationType.name
        }
        if (policyType != null) {
            whereSql += " and sp.polyRlmCd = :supportPolicyType"
            paramMap["supportPolicyType"] = SupportPolicyType.entries.first {
                it.value == policyType
            }.value
        }
        if (query != null) {
            whereSql += " and (sp.polyBizSjnm like CONCAT('%',:keyword,'%') or sp.polyItcnCn like CONCAT('%',:keyword,'%'))"
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
        var whereSql = " where true"
        val paramMap = mutableMapOf<String, String>()
        if (supportBusinessCategoryType != null && supportBusinessCategoryType != SupportBusinessCategoryType.전체) {
            whereSql += " and sb.category = :category"
            paramMap["category"] = supportBusinessCategoryType.name
        }
        if (registerYear != null) {
            whereSql += " and sb.registerYear = :registerYear"
            paramMap["registerYear"] = registerYear
        }
        if (query != null) {
            whereSql += " and (sb.title like CONCAT('%',:keyword,'%') or sb.content like CONCAT('%',:keyword,'%'))"
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
//        this.getSupportBusinessDetailImageUrl(supportBusiness.idx)
//        val img = ImageIO.read(ByteArrayInputStream(bytes))

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
            supportBusiness.registerYear,
            this.getSupportBusinessDetailImageUrl(supportBusiness.idx)
        )
    }

    private fun getSupportBusinessDetailImageUrl(supportBusinessIdx: Int): String {
        val document = Jsoup
            .connect("https://jaripon.ncrc.or.kr/home/kor/support/projectMng/edit.do")
            .method(Connection.Method.POST)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
            .data("menuPos", "1")
            .data("idx", supportBusinessIdx.toString())
            .post()
        val url = "https://jaripon.ncrc.or.kr" + document.select(".detail_view").select(".img_wrapper").select("img").attr("src")
        restTemplate.exchange("https://jaripon.ncrc.or.kr/home/kor/support/projectMng/index.do", HttpMethod.GET, HttpEntity("", HttpHeaders()), String::class.java)
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity("", HttpHeaders(
            LinkedMultiValueMap(
                mapOf(
                    "menuPos" to listOf("1")
                )
            )
        )), String::class.java)
//        val img = ImageIO.read(response.body?.inputStream())
        return "https://jaripon.ncrc.or.kr" + document.select(".detail_view").select(".img_wrapper").select("img").attr("src")
    }

    @Transactional(readOnly = true)
    fun getJobPostings(
        supportLocationType: SupportLocationType?,
        jobType: String?,
        query: String?
    ): List<JobPostingSummary> {
        val jpql = "select jp from JobPosting jp"
        var whereSql = " where true"
        val paramMap = mutableMapOf<String, String>()
        if (supportLocationType != null) {
            whereSql += " and jp.workRgnNmLst like CONCAT('%',:locationType,'%')"
            paramMap["locationType"] = supportLocationType.name
        }
        if (jobType != null) {
            whereSql += " and jp.ncsCdNmLst = :jobType"
            paramMap["jobType"] = jobType
        }
        if (query != null) {
            whereSql += " and jp.recrutPbancTtl like CONCAT('%',:keyword,'%')"
            paramMap["keyword"] = query
        }
        val sql = entityManager.createQuery(jpql + whereSql, JobPosting::class.java)
        paramMap.forEach {
            sql.setParameter(it.key, it.value)
        }
        val resultList = sql.resultList
        return resultList.map {
            JobPostingSummary(
                it.id!!,
                it.recrutPbancTtl,
                it.instNm,
                it.pbancEndYmd?.let {
                    "~${it.substring(4..5)}.${it.substring(6..7)}"
                },
                it.workRgnNmLst,
                it.acbgCondNmLst,
            )
        }
    }

    @Transactional(readOnly = true)
    fun getJobPostingDetail(jobPostingId: Long): JobPostingDetail {
        val jobPosting = jobPostingRepository.findById(jobPostingId).getOrNull()
            ?: throw Exception("해당 채용 공고를 찾을 수 없습니다.")
        return JobPostingDetail(
            jobPosting.id!!,
            jobPosting.acbgCondLst,
            jobPosting.acbgCondNmLst,
            jobPosting.aplyQlfcCn,
            jobPosting.decimalDay,
            jobPosting.disqlfcRsn,
            jobPosting.hireTypeLst,
            jobPosting.hireTypeNmLst,
            jobPosting.instNm,
            jobPosting.ncsCdLst,
            jobPosting.ncsCdNmLst,
            jobPosting.nonatchRsn,
            jobPosting.ongoingYn,
            jobPosting.pbadmsStdInstCd,
            jobPosting.pbancBgngYmd,
            jobPosting.pbancEndYmd,
            jobPosting.pblntInstCd,
            jobPosting.prefCn,
            jobPosting.prefCondCn,
            jobPosting.recrutNope,
            jobPosting.recrutPbancTtl,
            jobPosting.recrutPblntSn,
            jobPosting.recrutSe,
            jobPosting.recrutSeNm,
            jobPosting.replmprYn,
            jobPosting.scrnprcdrMthdExpln,
            jobPosting.srcUrl,
            jobPosting.workRgnLst,
            jobPosting.workRgnNmLst,
        )
    }
}