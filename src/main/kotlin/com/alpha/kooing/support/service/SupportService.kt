package com.alpha.kooing.support.service

import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.support.*
import com.alpha.kooing.support.dto.*
import com.alpha.kooing.support.entity.*
import com.alpha.kooing.support.enum.SupportBusinessCategoryType
import com.alpha.kooing.support.enum.SupportLocationType
import com.alpha.kooing.support.enum.SupportPolicyType
import com.alpha.kooing.support.event.Scrap3JobPostingEvent
import com.alpha.kooing.support.event.Scrap3SupportBusinessEvent
import com.alpha.kooing.support.event.Scrap3SupportPolicyEvent
import com.alpha.kooing.support.repository.*
import com.alpha.kooing.user.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import org.apache.commons.io.FileUtils
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLConnection
import kotlin.jvm.optionals.getOrNull


@Service
class SupportService(
    private val supportPolicyRepository: SupportPolicyRepository,
    private val supportBusinessRepository: SupportBusinessRepository,
    private val jobPostingRepository: JobPostingRepository,
    private val userRepository: UserRepository,
    private val entityManager: EntityManager,
    private val restTemplate: RestTemplate,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jobPostingScrapRepository: JobPostingScrapRepository,
    private val supportBusinessScrapRepository: SupportBusinessScrapRepository,
    private val supportPolicyScrapRepository: SupportPolicyScrapRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Transactional(readOnly = true)
    fun getSupportPolicies(
        token: String,
        supportLocationType: SupportLocationType?,
        policyType: String?,
        query: String?,
        scrap: Boolean?,
    ): List<SupportPolicySummary> {
        val supportPolicyList = if (scrap == true) {
            val userEmail = jwtTokenProvider.getJwtEmail(token)
            val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
            user.supportPolicyScraps.map {
                it.supportPolicy
            }
        } else {
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
            sql.resultList
        }
        return supportPolicyList.map {
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
        token: String,
        supportBusinessCategoryType: SupportBusinessCategoryType?,
        registerYear: String?,
        query: String?,
        scrap: Boolean?,
    ): List<SupportBusinessSummary> {
        val supportBusinessList = if (scrap == true) {
            val userEmail = jwtTokenProvider.getJwtEmail(token)
            val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
            user.supportBusinessScraps.map {
                it.supportBusiness
            }
        } else {
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
            sql.resultList
        }
        return supportBusinessList.map {
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
        token: String,
        supportLocationType: SupportLocationType?,
        jobType: String?,
        query: String?,
        scrap: Boolean?,
    ): List<JobPostingSummary> {
        val jobPostingList = if (scrap == true) {
            val userEmail = jwtTokenProvider.getJwtEmail(token)
            val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
            user.jobPostingScraps.map {
                it.jobPosting
            }
        } else {
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
            sql.resultList
        }
        return jobPostingList.map {
            JobPostingSummary(
                it.id!!,
                it.recrutPbancTtl,
                it.instNm,
//                it.pbancEndYmd?.let {
//                    "~${it.substring(4..5)}.${it.substring(6..7)}"
//                },
                it.workRgnNmLst,
                it.acbgCondNmLst,
                it.recrutSeNm
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

    @Transactional
    fun scrapSupportPolicy(token: String, supportPolicyId: Long) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        val supportPolicy = supportPolicyRepository.findById(supportPolicyId).getOrNull() ?: throw Exception("지원 정책 정보가 올바르지 않습니다.")
        supportPolicyScrapRepository.save(
            SupportPolicyScrap(
                null, user, supportPolicy
            )
        )
        if (user.supportPolicyScraps.size == 3) {
            applicationEventPublisher.publishEvent(Scrap3SupportPolicyEvent(user))
        }
    }

    @Transactional
    fun scrapSupportBusiness(token: String, supportBusinessId: Long) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        val supportBusiness = supportBusinessRepository.findById(supportBusinessId).getOrNull() ?: throw Exception("지원 사업 정보가 올바르지 않습니다.")
        supportBusinessScrapRepository.save(
            SupportBusinessScrap(
                null, user, supportBusiness
            )
        )
        if (user.supportBusinessScraps.size == 3) {
            applicationEventPublisher.publishEvent(Scrap3SupportBusinessEvent(user))
        }
    }

    @Transactional
    fun scrapJobPosting(token: String, jobPostingId: Long) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        val jobPosting = jobPostingRepository.findById(jobPostingId).getOrNull() ?: throw Exception("채용 공고 정보가 올바르지 않습니다.")
        jobPostingScrapRepository.save(
            JobPostingScrap(
                null, user, jobPosting
            )
        )
        if (user.jobPostingScraps.size == 3) {
            applicationEventPublisher.publishEvent(Scrap3JobPostingEvent(user))
        }
    }

    fun getSupportBusinessDetailFile(supportBusinessId: Long, fileName: String, downName: String): SupportBusinessDetailFileDto {
        val url = "https://jaripon.ncrc.or.kr/fileDownload.do"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("filename", fileName)
        map.add("downname", downName)
        map.add("uniqueKey", "")
        val response = restTemplate.postForEntity(url, HttpEntity(map, headers), ByteArray::class.java)
        return SupportBusinessDetailFileDto(
            response.body!!, URLConnection.guessContentTypeFromName(fileName)
        )
    }

    fun byteArrayToFile(byteArray: ByteArray, filePath: String): File {
        val file = File(filePath)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            fos.write(byteArray)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
        return file
    }
}