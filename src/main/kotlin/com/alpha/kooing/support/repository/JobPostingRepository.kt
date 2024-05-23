package com.alpha.kooing.support.repository

import com.alpha.kooing.support.entity.JobPosting
import org.springframework.data.jpa.repository.JpaRepository

interface JobPostingRepository: JpaRepository<JobPosting, Long>