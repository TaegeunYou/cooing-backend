package com.alpha.kooing.support.entity

import jakarta.persistence.*

@Entity
class JobPosting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(columnDefinition = "text")
    val recrutPblntSn: String? = null,
    @Column(columnDefinition = "text")
    val pblntInstCd: String? = null,
    @Column(columnDefinition = "text")
    val pbadmsStdInstCd: String? = null,
    @Column(columnDefinition = "text")
    val instNm: String? = null,
    @Column(columnDefinition = "text")
    val ncsCdLst: String? = null,
    @Column(columnDefinition = "text")
    val ncsCdNmLst: String? = null,
    @Column(columnDefinition = "text")
    val hireTypeLst: String? = null,
    @Column(columnDefinition = "text")
    val hireTypeNmLst: String? = null,
    @Column(columnDefinition = "text")
    val workRgnLst: String? = null,
    @Column(columnDefinition = "text")
    val workRgnNmLst: String? = null,
    @Column(columnDefinition = "text")
    val recrutSe: String? = null,
    @Column(columnDefinition = "text")
    val recrutSeNm: String? = null,
    @Column(columnDefinition = "text")
    val prefCondCn: String? = null,
    @Column(columnDefinition = "text")
    val recrutNope: String? = null,
    @Column(columnDefinition = "text")
    val pbancBgngYmd: String? = null,
    @Column(columnDefinition = "text")
    val pbancEndYmd: String? = null,
    @Column(columnDefinition = "text")
    val recrutPbancTtl: String? = null,
    @Column(columnDefinition = "text")
    val srcUrl: String? = null,
    @Column(columnDefinition = "text")
    val replmprYn: String? = null,
    @Column(columnDefinition = "text")
    val aplyQlfcCn: String? = null,
    @Column(columnDefinition = "text")
    val disqlfcRsn: String? = null,
    @Column(columnDefinition = "text")
    val scrnprcdrMthdExpln: String? = null,
    @Column(columnDefinition = "text")
    val prefCn: String? = null,
    @Column(columnDefinition = "text")
    val acbgCondLst: String? = null,
    @Column(columnDefinition = "text")
    val acbgCondNmLst: String? = null,
    @Column(columnDefinition = "text")
    val nonatchRsn: String? = null,
    @Column(columnDefinition = "text")
    val ongoingYn: String? = null,
    @Column(columnDefinition = "text")
    val decimalDay: String? = null
)