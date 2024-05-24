package com.alpha.kooing.support.dto

class SupportPolicyDto(
    val youthPolicyList: YouthPolicyList? = null
) {
    class YouthPolicyList(
        val pageIndex: Int? = null,
        val totalCnt: Int? = null,
        val youthPolicy: List<YouthPolicy>? = null
    ) {
        class YouthPolicy(
            val bizId: String? = null, //	정책 ID
            val polyBizSecd: String? = null, //	기관코드

            val polyBizSjnm: String? = null, //	정책명
            val polyItcnCn: String? = null, //	정책소개
            val polyRlmCd: String? = null,  //정책분야코드    1. 일자리 분야(023010) 2. 주거 분야(023020) 3. 교육 분야(023030) 4. 복지.문화 분야(023040) 5. 참여.권리 분야(023050)
            val sporCn: String? = null, //	지원내용
            val rqutPrdCn: String? = null, //	사업신청기간내용
            val bizPrdCn: String? = null, //	사업운영기간내용
            val sporScvl: String? = null, //	지원규모
            val prdRpttSecd: String? = null, //	사업신청기간반복구분코드    1. 상시(002001) 2. 연간반복(002002) 3. 월간반복(002003) 4. 특정기간(002004) 5. 미정(002005)

            //신청자격
            val ageInfo: String? = null, //	연령 정보
            val prcpCn: String? = null, //	거주지및소득조건내용
            val accrRqisCn: String? = null, //	학력요건내용
            val majrRqisCn: String? = null, //	전공요건내용
            val empmSttsCn: String? = null, //	취업상태내용
            val splzRlmRqisCn: String? = null, //	특화분야내용
            val aditRscn: String? = null, //	추가단서사항내용
            val prcpLmttTrgtCn: String? = null, //	참여제한대상내용

            //신청방법
            val rqutProcCn: String? = null, //	신청절차내용
            val jdgnPresCn: String? = null, //	심사발표내용
            val rqutUrla: String? = null, //	신청사이트주소
            val pstnPaprCn: String? = null, //	제출서류내용

            //기타
            val etct: String? = null, //	기타사항
            val mngtMson: String? = null, //	주관부서명
            val cnsgNmor: String? = null, //	운영기관명
            val rfcSiteUrla1: String? = null, //	참고사이트URL주소1
            val rfcSiteUrla2: String? = null, //	참고사이트URL주소2
        )

        class YouthPolicyDetail(
            val rnum: Int? = null, //	row 번호
            val bizId: String? = null, //	정책 ID
            val polyBizSecd: String? = null, //	기관코드
            val polyBizTy: String? = null, //	기관 및 지자체 구분
            val polyBizSjnm: String? = null, //	정책명
            val polyItcnCn: String? = null, //	정책소개
            val sporCn: String? = null, //	지원내용
            val sporScvl: String? = null, //	지원규모
            val bizPrdCn: String? = null, //	사업운영기간내용
            val prdRpttSecd: String? = null, //	사업신청기간반복구분코드    1. 상시(002001) 2. 연간반복(002002) 3. 월간반복(002003) 4. 특정기간(002004) 5. 미정(002005)
            val rqutPrdCn: String? = null, //	사업신청기간내용
            val ageInfo: String? = null, //	연령 정보
            val majrRqisCn: String? = null, //	전공요건내용
            val empmSttsCn: String? = null, //	취업상태내용
            val splzRlmRqisCn: String? = null, //	특화분야내용
            val accrRqisCn: String? = null, //	학력요건내용
            val prcpCn: String? = null, //	거주지및소득조건내용
            val aditRscn: String? = null, //	추가단서사항내용
            val prcpLmttTrgtCn: String? = null, //	참여제한대상내용
            val rqutProcCn: String? = null, //	신청절차내용
            val pstnPaprCn: String? = null, //	제출서류내용
            val jdgnPresCn: String? = null, //	심사발표내용
            val rqutUrla: String? = null, //	신청사이트주소
            val rfcSiteUrla1: String? = null, //	참고사이트URL주소1
            val rfcSiteUrla2: String? = null, //	참고사이트URL주소2
            val mngtMson: String? = null, //	주관부서명
            val mngtMrofCherCn: String? = null, //	주관부서담당자이름
            val cherCtpcCn: String? = null, //	주관부서담당자연락처
            val cnsgNmor: String? = null, //	운영기관명
            val tintCherCn: String? = null, //	운영기관담당자이름
            val tintCherCtpcCn: String? = null, //	운영기관담당자연락처
            val etct: String? = null, //	기타사항
            val polyRlmCd: String? = null,  //정책분야코드    1. 일자리 분야(023010) 2. 주거 분야(023020) 3. 교육 분야(023030) 4. 복지.문화 분야(023040) 5. 참여.권리 분야(023050)
        )
    }
}