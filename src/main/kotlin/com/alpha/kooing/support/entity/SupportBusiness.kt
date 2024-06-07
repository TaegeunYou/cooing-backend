package com.alpha.kooing.support.entity

import jakarta.persistence.*

@Entity
class SupportBusiness(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "text")
    val title: String,      //제목
    @Column(columnDefinition = "text")
    val category: String?,   //분야
    @Column(columnDefinition = "text")
    val location: String, // 지역
    @Column(columnDefinition = "text")
    val recruitPeriod: String, // 모집기간
    @Column(columnDefinition = "text")
    val resultDate: String, // 결과발표일
    @Column(columnDefinition = "text")
    val receptionMethod: String, // 접수방법
    @Column(columnDefinition = "text")
    val checkResultMethod: String, // 결과확인방법
    @Column(columnDefinition = "text")
    val content: String, // 내용
    @Column(columnDefinition = "text")
    val organizationName: String, // 기관명
    @Column(columnDefinition = "text")
    val organizationPerson: String, // 담당자명
    @Column(columnDefinition = "text")
    val organizationContact: String, // 문의
    @Column(columnDefinition = "text")
    val filesJson: String,
    @Column(columnDefinition = "text")
    val registerYear: String,
    @Column(columnDefinition = "text")
    val idx: Int,
    @Column(columnDefinition = "text")
    var imageUrl: String? = null,
) {

    fun updateImageUrl(imageUrl: String) {
        this.imageUrl = imageUrl
    }
}