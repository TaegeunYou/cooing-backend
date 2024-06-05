package com.alpha.kooing.support.service

import com.alpha.kooing.support.dto.SupportBusinessDto
import com.alpha.kooing.support.dto.SupportBusinessIdAndYearDto
import com.alpha.kooing.support.repository.SupportBusinessRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement
import java.sql.SQLException


@Service
class SupportBusinessSchedulingService(
    private val supportBusinessRepository: SupportBusinessRepository,
    private val jdbcTemplate: JdbcTemplate,
) {

    @Transactional
    fun updateSupportBusiness() {
        val externalIds = getAllSupportBusinessExternalIds()
        val supportBusinessList = externalIds.map {
            parseSupportBusinessDetail(
                requestSupportBusinessDetail(it.id),
                it.year,
                it.id
            )
        }
        val idxIds = externalIds.map { it.id }
        //모두 삭제하고 모두 다시 추가하는 방식
//        supportBusinessRepository.deleteAllInBatch()
//        this.bulkInsertSupportBusiness(supportBusinessList)
        //없는 것만 추가하는 방식
        this.bulkInsertSupportBusiness(
            supportBusinessList.filter {
                it.idx !in idxIds
            }
        )
    }

    /**
     * 첫 페이지를 조회하여 등록년도를 모두 가져와서
     * 등록년도 마다 최대 페이지까지 조회해서
     * 지원사업 외부 id와 등록년도 쌍을 모두 가져온다.
     */
    private fun getAllSupportBusinessExternalIds(): List<SupportBusinessIdAndYearDto> {
        val supportBusinessExternalIds = mutableListOf<SupportBusinessIdAndYearDto>()
        val firstPageDocument = this.requestSupportBusinessList(1)
        val registerYears = parseRegisterYears(firstPageDocument)
        registerYears.forEach { registerYear ->
            val firstDocument = this.requestSupportBusinessList(1, registerYear)
            val firstDocumentExternalIds = this.parseExternalIds(firstDocument)
            val maxPage = firstDocument.select(".synthesis").select(".num").text().toInt() / 12 + 1
            supportBusinessExternalIds.addAll(
                firstDocumentExternalIds.map {
                    SupportBusinessIdAndYearDto(it, registerYear)
                }
            )
            (2..maxPage).forEach { page ->
                val document = this.requestSupportBusinessList(page, registerYear)
                val externalIds = this.parseExternalIds(document)
                supportBusinessExternalIds.addAll(
                    externalIds.map {
                        SupportBusinessIdAndYearDto(it, registerYear)
                    }
                )
            }
        }
        return supportBusinessExternalIds
    }

    private fun requestSupportBusinessList(page: Int, registerYear: String? = null): Document {
        return Jsoup
            .connect("https://jaripon.ncrc.or.kr/home/kor/support/projectMng/index.do")
            .method(Connection.Method.POST)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
            .data("menuPos", "1")
            .data("pageIndex", page.toString())
            .also {
                if (registerYear != null) {
                    it.data("searchValue5", registerYear)
                }
            }
            .post()
    }

    private fun parseExternalIds(document: Document): List<Int> {
        return document
            .select(".gallery_list")
            .select("a")
            .map {
                it.attr("onclick").split("'")[1].toInt()
            }
    }

    private fun parseRegisterYears(document: Document): List<String> {
        return document
            .select("#searchValue5")
            .select("option")
            .mapNotNull {
                it.attr("value").takeIf { it != "" }
            }
    }

    private fun requestSupportBusinessDetail(id: Int): Document {
        val document = Jsoup
            .connect("https://jaripon.ncrc.or.kr/home/kor/support/projectMng/edit.do")
            .method(Connection.Method.POST)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
            .data("menuPos", "1")
            .data("idx", id.toString())
            .post()
        return document
    }

    private fun parseSupportBusinessDetail(document: Document, registerYear: String, idx: Int): SupportBusinessDto {
        val title = document.select(".text_area").select(".title_area").select(".title").text()
        val category = document.select(".info_box").select(".icon01").select(".text_wrap").text()
        val location = document.select(".info_box").select(".icon02").select(".text_wrap").text()
        val recruitPeriod = document.select(".info_box").select(".icon03").select(".text_wrap").text()
        val resultDate = document.select(".info_box").select(".icon04").select(".text_wrap").text()
        val receptionMethod = document.select(".info_box").select(".icon06").select(".text_wrap").text()
        val checkResultMethod = document.select(".info_box").select(".icon05").select(".text_wrap").text()
        document.select(".editor_view").select("br").forEach { it.appendText("줄바꿈") }
        document.select(".editor_view").text().replace("&nbsp;", "")
        val content = document.select(".editor_view").text().replace("줄바꿈", "\n")
        val organizationName = document.select(".comp_inner").select(".comp_txt")[0].text()
        val organizationPerson = document.select(".comp_inner").select(".comp_txt")[1].text()
        val organizationContact = document.select(".comp_inner").select(".comp_txt")[2].text()
        val files = document.select(".board_view_file").select(".file_each").map {
            val attrList = it.select("a").attr("onclick").split("'")
            val fileName = attrList[1]
            val downName = attrList[3]
            Pair(fileName, downName)
        }
        return SupportBusinessDto(
            title,
            category.takeIf { it.isNotEmpty() },
            location,
            recruitPeriod,
            resultDate,
            receptionMethod,
            checkResultMethod,
            content,
            organizationName,
            organizationPerson,
            organizationContact,
            files.map {
                SupportBusinessDto.SupportBusinessFile(it.first, it.second)
            },
            registerYear,
            idx
        )
    }

    private fun bulkInsertSupportBusiness(supportBusinessList: List<SupportBusinessDto>) {
        val mapper = ObjectMapper()
        val sql = ("insert into "
                + "support_business "
                + "(title, category, location, recruit_period, result_date, reception_method, check_result_method, content, "
                + "organization_name, organization_person, organization_contact, files_json, register_year, idx)"
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)")

        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val supportBusiness = supportBusinessList[i]
                ps.setString(1, supportBusiness.title)
                ps.setString(2, supportBusiness.category)
                ps.setString(3, supportBusiness.location)
                ps.setString(4, supportBusiness.recruitPeriod)
                ps.setString(5, supportBusiness.resultDate)
                ps.setString(6, supportBusiness.receptionMethod)
                ps.setString(7, supportBusiness.checkResultMethod)
                ps.setString(8, supportBusiness.content)
                ps.setString(9, supportBusiness.organizationName)
                ps.setString(10, supportBusiness.organizationPerson)
                ps.setString(11, supportBusiness.organizationContact)
                ps.setString(12, supportBusiness.files.map {
                    mapper.writeValueAsString(it)
                }.toString())
                ps.setString(13, supportBusiness.registerYear)
                ps.setInt(14, supportBusiness.idx)
            }

            override fun getBatchSize(): Int {
                return supportBusinessList.size
            }
        })
    }
}