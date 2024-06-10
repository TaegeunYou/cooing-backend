package com.alpha.kooing.support.service

import JobPostingDto
import com.alpha.kooing.support.repository.JobPostingRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.sql.PreparedStatement
import java.sql.SQLException

@Service
class JobPostingSchedulingService(
    private val restTemplate: RestTemplate,
    private val jdbcTemplate: JdbcTemplate,
    private val jobPostingRepository: JobPostingRepository,
) {

    var totalCount = 0
    val numOfRows = 1000        //요청당 최대 받을 수 있는 row 개수 1000

    @Transactional
    fun updateJobPosting() {
        val jobPostingList = mutableListOf<JobPostingDto.RecruitmentResult>()
        val firstResult = getJobPostingByExternal(1)
        jobPostingList.addAll(firstResult.result!!)
        val maxPageNo = totalCount / numOfRows + 1
        (2..maxPageNo).forEach { pageNo ->
            jobPostingList.addAll(
                getJobPostingByExternal(pageNo).result!!
            )
        }
        //모두 삭제하고 모두 다시 추가하는 방식
//        jobPostingRepository.deleteAllInBatch()
//        this.bulkInsertJobPosting(jobPostingList)
        //없는 것만 추가하는 방식
        val recrutPblntSns = jobPostingRepository.getAllRecrutPblntSn()
        this.bulkInsertJobPosting(
            jobPostingList.filter {
                it.recrutPblntSn !in recrutPblntSns
            }
        )
    }

    private fun getJobPostingByExternal(pageNo: Int): JobPostingDto {
        val builder = StringBuilder("https://apis.data.go.kr/1051000/recruitment/list")
        builder.append("?serviceKey=ivxZb0bI3o3GR%2FGSTAEMVZyVs%2F0cnOCpzc4TI2MG0eYYjYTYeIDvId9KGBdOLHeWxAnIz7R482On8aZIUYzRew%3D%3D")
        builder.append("&numOfRows=$numOfRows")
        builder.append("&ongoingYn=Y")
        builder.append("&pageNo=$pageNo")
        val url: URI = URI.create(builder.toString())
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity("", HttpHeaders()), String::class.java)
        val mapper = ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.readValue(response.body.toString(), JobPostingDto::class.java).also {
            if (totalCount == 0) {
                totalCount = it.totalCount!!
            }
        }
    }

    private fun bulkInsertJobPosting(jobPostingList: List<JobPostingDto.RecruitmentResult>) {
        val sql = ("insert into job_posting "
                + "(recrut_pblnt_sn, pblnt_inst_cd, pbadms_std_inst_cd, inst_nm, ncs_cd_lst, ncs_cd_nm_lst, hire_type_lst, hire_type_nm_lst, "
                + "work_rgn_lst, work_rgn_nm_lst, recrut_se, recrut_se_nm, pref_cond_cn, recrut_nope, pbanc_bgng_ymd, pbanc_end_ymd, "
                + "recrut_pbanc_ttl, src_url, replmpr_yn, aply_qlfc_cn, disqlfc_rsn, scrnprcdr_mthd_expln, pref_cn, acbg_cond_lst, acbg_cond_nm_lst, "
                + "nonatch_rsn, ongoing_yn, decimal_day) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val jobPosting = jobPostingList[i]
                ps.setString(1, jobPosting.recrutPblntSn)
                ps.setString(2, jobPosting.pblntInstCd)
                ps.setString(3, jobPosting.pbadmsStdInstCd)
                ps.setString(4, jobPosting.instNm)
                ps.setString(5, jobPosting.ncsCdLst)
                ps.setString(6, jobPosting.ncsCdNmLst)
                ps.setString(7, jobPosting.hireTypeLst)
                ps.setString(8, jobPosting.hireTypeNmLst)
                ps.setString(9, jobPosting.workRgnLst)
                ps.setString(10, jobPosting.workRgnNmLst)
                ps.setString(11, jobPosting.recrutSe)
                ps.setString(12, jobPosting.recrutSeNm)
                ps.setString(13, jobPosting.prefCondCn)
                ps.setString(14, jobPosting.recrutNope)
                ps.setString(15, jobPosting.pbancBgngYmd)
                ps.setString(16, jobPosting.pbancEndYmd)
                ps.setString(17, jobPosting.recrutPbancTtl)
                ps.setString(18, jobPosting.srcUrl)
                ps.setString(19, jobPosting.replmprYn)
                ps.setString(20, jobPosting.aplyQlfcCn)
                ps.setString(21, jobPosting.disqlfcRsn)
                ps.setString(22, jobPosting.scrnprcdrMthdExpln)
                ps.setString(23, jobPosting.prefCn)
                ps.setString(24, jobPosting.acbgCondLst)
                ps.setString(25, jobPosting.acbgCondNmLst)
                ps.setString(26, jobPosting.nonatchRsn)
                ps.setString(27, jobPosting.ongoingYn)
                ps.setString(28, jobPosting.decimalDay)
            }

            override fun getBatchSize(): Int {
                return jobPostingList.size
            }
        })
    }
}