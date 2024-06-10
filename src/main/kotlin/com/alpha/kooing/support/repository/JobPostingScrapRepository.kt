package com.alpha.kooing.support.repository

import com.alpha.kooing.support.entity.JobPostingScrap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface JobPostingScrapRepository: JpaRepository<JobPostingScrap, Long>{
    @Modifying
    @Query("delete from JobPostingScrap j where j.jobPosting.id=:jobPostId AND j.user.id=:userId")
    fun deleteByUserIdAndJobPostId(userId: Long, jobPostId: Long)
}