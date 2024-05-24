package com.alpha.kooing.support.dto

class SupportBusinessDto(
    val title: String,      //제목
    val category: String?,   //분야
    val location: String, // 지역
    val recruitPeriod: String, // 모집기간
    val resultDate: String, // 결과발표일
    val receptionMethod: String, // 접수방법
    val checkResultMethod: String, // 결과확인방법
    val content: String, // 내용
    val organizationName: String, // 기관명
    val organizationPerson: String, // 담당자명
    val organizationContact: String, // 문의
    val files: List<SupportBusinessFile>,    //첨부파일
    var registerYear: String,
) {
    class SupportBusinessFile(
        val fileName: String,
        val downName: String
    )
}