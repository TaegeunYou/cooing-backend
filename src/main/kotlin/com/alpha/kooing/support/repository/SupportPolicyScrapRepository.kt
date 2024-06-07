package com.alpha.kooing.support.repository

import com.alpha.kooing.support.entity.SupportPolicyScrap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface SupportPolicyScrapRepository: JpaRepository<SupportPolicyScrap, Long>{
    @Modifying
    @Query("delete from SupportPolicyScrap s where s.supportPolicy.id=:policyId AND s.user.id=:userId")
    fun deleteByUserIdAndPolicyId(userId: Long, policyId: Long)
}