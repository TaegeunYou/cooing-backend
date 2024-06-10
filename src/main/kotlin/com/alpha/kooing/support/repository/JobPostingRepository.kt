package com.alpha.kooing.support.repository

import com.alpha.kooing.support.entity.JobPosting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface JobPostingRepository: JpaRepository<JobPosting, Long> {
    @Query("select jp.recrutPblntSn from JobPosting jp")
    fun getAllRecrutPblntSn(): List<String?>
}