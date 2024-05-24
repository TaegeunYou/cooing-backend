package com.alpha.kooing.support.entity

import com.alpha.kooing.support.dto.SupportPolicyDto
import jakarta.persistence.*

@Entity
class SupportPolicy(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "text")
    val bizId: String? = null, //	정책 ID
    @Column(columnDefinition = "text")
    val polyBizSecd: String? = null,    //지역
    @Column(columnDefinition = "text")
    val polyBizSjnm: String? = null, //	정책명
    @Column(columnDefinition = "text")
    val polyItcnCn: String? = null, //	정책소개
    @Column(columnDefinition = "text")
    val polyRlmCd: String? = null,  //정책분야코드    1. 일자리 분야(023010) 2. 주거 분야(023020) 3. 교육 분야(023030) 4. 복지.문화 분야(023040) 5. 참여.권리 분야(023050)
    @Column(columnDefinition = "text")
    val sporCn: String? = null, //	지원내용
    @Column(columnDefinition = "text")
    val rqutPrdCn: String? = null, //	사업신청기간내용
    @Column(columnDefinition = "text")
    val bizPrdCn: String? = null, //	사업운영기간내용
    @Column(columnDefinition = "text")
    val sporScvl: String? = null, //	지원규모
    @Column(columnDefinition = "text")
    val prdRpttSecd: String? = null, //	사업신청기간반복구분코드    1. 상시(002001) 2. 연간반복(002002) 3. 월간반복(002003) 4. 특정기간(002004) 5. 미정(002005)

    //신청자격
    @Column(columnDefinition = "text")
    val ageInfo: String? = null, //	연령 정보
    @Column(columnDefinition = "text")
    val prcpCn: String? = null, //	거주지및소득조건내용
    @Column(columnDefinition = "text")
    val accrRqisCn: String? = null, //	학력요건내용
    @Column(columnDefinition = "text")
    val majrRqisCn: String? = null, //	전공요건내용
    @Column(columnDefinition = "text")
    val empmSttsCn: String? = null, //	취업상태내용
    @Column(columnDefinition = "text")
    val splzRlmRqisCn: String? = null, //	특화분야내용
    @Column(columnDefinition = "text")
    val aditRscn: String? = null, //	추가단서사항내용
    @Column(columnDefinition = "text")
    val prcpLmttTrgtCn: String? = null, //	참여제한대상내용

    //신청방법
    @Column(columnDefinition = "text")
    val rqutProcCn: String? = null, //	신청절차내용
    @Column(columnDefinition = "text")
    val jdgnPresCn: String? = null, //	심사발표내용
    @Column(columnDefinition = "text")
    val rqutUrla: String? = null, //	신청사이트주소
    @Column(columnDefinition = "text")
    val pstnPaprCn: String? = null, //	제출서류내용

    //기타
    @Column(columnDefinition = "text")
    val etct: String? = null, //	기타사항
    @Column(columnDefinition = "text")
    val mngtMson: String? = null, //	주관부서명
    @Column(columnDefinition = "text")
    val cnsgNmor: String? = null, //	운영기관명
    @Column(columnDefinition = "text")
    val rfcSiteUrla1: String? = null, //	참고사이트URL주소1
    @Column(columnDefinition = "text")
    val rfcSiteUrla2: String? = null, //	참고사이트URL주소2
)