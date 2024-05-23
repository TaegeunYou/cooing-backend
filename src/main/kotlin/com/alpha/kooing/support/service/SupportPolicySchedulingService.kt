package com.alpha.kooing.support.service

import com.alpha.kooing.support.dto.SupportPolicyDto
import com.alpha.kooing.support.enum.SupportLocationType
import com.alpha.kooing.support.enum.SupportPolicyPeriodType
import com.alpha.kooing.support.enum.SupportPolicyType
import com.alpha.kooing.support.repository.SupportPolicyRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.json.XML
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.sql.PreparedStatement
import java.sql.SQLException


@Service
class SupportPolicySchedulingService(
    private val restTemplate: RestTemplate,
    private val supportPolicyRepository: SupportPolicyRepository,
    private val jdbcTemplate: JdbcTemplate,
) {

    /**
     * 온통 청년 사이트에서 오픈 api로 제공해주는 지원 정책을 가져와서 DB에 저장
     */
    @Transactional
    fun updateSupportPolicy() {
        val supportPolicies = this.getAllSupportPolicyByExternal()
        if (supportPolicies.isNotEmpty()) {
            //모두 삭제하고 모두 다시 추가하는 방식
            supportPolicyRepository.deleteAllInBatch()
            this.bulkInsertSupportPolicy(supportPolicies)

            //없는 것만 추가하는 방식
//            val bizIds = supportPolicyRepository.getAllBizIds()
//            this.bulkInsertSupportPolicy(
//                supportPolicies.filter {
//                    it.bizId !in bizIds
//                }
//            )
        }
    }

    private fun getAllSupportPolicyByExternal(): List<SupportPolicyDto.YouthPolicyList.YouthPolicy> {
        val display = 100   //한 번에 가져올 수 있는 최대값
        var totalPageIndex = 1  //현재 페이지 값
        val supportPolicies = mutableListOf<SupportPolicyDto.YouthPolicyList.YouthPolicy>()
        val supportPolicy = this.getSupportPolicyByExternal(display, totalPageIndex)
        supportPolicies.addAll(supportPolicy.youthPolicyList!!.youthPolicy!!)
        totalPageIndex = supportPolicy.youthPolicyList.totalCnt!! / display + 1
        for (pageIndex in 2..totalPageIndex) {
            this.getSupportPolicyByExternal(display, pageIndex)
            supportPolicies.addAll(supportPolicy.youthPolicyList.youthPolicy!!)
        }
        return supportPolicies
    }

    private fun getSupportPolicyByExternal(display: Int, pageIndex: Int): SupportPolicyDto {
        val url = UriComponentsBuilder
            .fromUriString("https://www.youthcenter.go.kr/opi/youthPlcyList.do")
            .queryParam("openApiVlak", "d221a60747cd729ea99db9ea")
            .queryParam("display", display)
            .queryParam("pageIndex", pageIndex)
            .encode().build().toUriString()
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity("", HttpHeaders()), String::class.java)
        return try {
            this.xmlToObject(response.body.toString(), SupportPolicyDto::class.java)
        } catch (e: Exception) {
            println(response.body.toString())
            throw e
        }
    }

    private fun <T> xmlToObject(xml: String, clazz: Class<T>): T {
        val mapper = ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.readValue(XML.toJSONObject(xml).toString(), clazz)
    }

    private fun bulkInsertSupportPolicy(supportPolicyList: List<SupportPolicyDto.YouthPolicyList.YouthPolicy>) {
        val sql = ("insert into "
                + "support_policy "
                + "(poly_biz_sjnm, poly_itcn_cn, poly_rlm_cd, spor_cn, rqut_prd_cn, biz_prd_cn, spor_scvl, prd_rptt_secd, "
                + "age_info, prcp_cn, accr_rqis_cn, majr_rqis_cn, empm_stts_cn, splz_rlm_rqis_cn, adit_rscn, prcp_lmtt_trgt_cn, "
                + "rqut_proc_cn, jdgn_pres_cn, rqut_urla, pstn_papr_cn, etct, mngt_mson, cnsg_nmor, rfc_site_urla1, rfc_site_urla2, poly_biz_secd, biz_id) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")

        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val supportPolicy = supportPolicyList[i]
                ps.setString(1, supportPolicy.polyBizSjnm.noContentIsNull())
                ps.setString(2, supportPolicy.polyItcnCn.noContentIsNull())
                ps.setString(3, supportPolicy.polyRlmCd.noContentIsNull().policyCodeToName())
                ps.setString(4, supportPolicy.sporCn.noContentIsNull())
                ps.setString(5, supportPolicy.rqutPrdCn.noContentIsNull())
                ps.setString(6, supportPolicy.bizPrdCn.noContentIsNull())
                ps.setString(7, supportPolicy.sporScvl.noContentIsNull())
                ps.setString(8, supportPolicy.prdRpttSecd.noContentIsNull().periodCodeToName())
                ps.setString(9, supportPolicy.ageInfo.noContentIsNull())
                ps.setString(10, supportPolicy.prcpCn.noContentIsNull())
                ps.setString(11, supportPolicy.accrRqisCn.noContentIsNull())
                ps.setString(12, supportPolicy.majrRqisCn.noContentIsNull())
                ps.setString(13, supportPolicy.empmSttsCn.noContentIsNull())
                ps.setString(14, supportPolicy.splzRlmRqisCn.noContentIsNull())
                ps.setString(15, supportPolicy.aditRscn.noContentIsNull())
                ps.setString(16, supportPolicy.prcpLmttTrgtCn.noContentIsNull())
                ps.setString(17, supportPolicy.rqutProcCn.noContentIsNull())
                ps.setString(18, supportPolicy.jdgnPresCn.noContentIsNull())
                ps.setString(19, supportPolicy.rqutUrla.noContentIsNull())
                ps.setString(20, supportPolicy.pstnPaprCn.noContentIsNull())
                ps.setString(21, supportPolicy.etct.noContentIsNull())
                ps.setString(22, supportPolicy.mngtMson.noContentIsNull())
                ps.setString(23, supportPolicy.cnsgNmor.noContentIsNull())
                ps.setString(24, supportPolicy.rfcSiteUrla1.noContentIsNull())
                ps.setString(25, supportPolicy.rfcSiteUrla2.noContentIsNull())
                ps.setString(26, supportPolicy.polyBizSecd.noContentIsNull().locationCodeToName())
                ps.setString(27, supportPolicy.bizId.noContentIsNull())
            }

            override fun getBatchSize(): Int {
                return supportPolicyList.size
            }
        })
    }

    private fun String?.noContentIsNull(): String? {
        return if (this in listOf(null, "-", "null")) {
            null
        } else {
            this
        }
    }

    private fun String?.locationCodeToName(): String? {
        if (this == null) return null
        val supportLocationType = SupportLocationType.entries.firstOrNull { locationType ->
            this.contains(locationType.supportPolicyCode)
        }
        if (supportLocationType == null) {
            return this
        }
        return supportLocationType.name
    }

    private fun String?.policyCodeToName(): String? {
        if (this == null) return null
        return SupportPolicyType.entries.firstOrNull { policyType ->
            this == policyType.code
        }?.value ?: this
    }

    private fun String?.periodCodeToName(): String? {
        if (this == null) return null
        return SupportPolicyPeriodType.entries.firstOrNull { periodType ->
            this == periodType.code
        }?.name ?: this
    }
}