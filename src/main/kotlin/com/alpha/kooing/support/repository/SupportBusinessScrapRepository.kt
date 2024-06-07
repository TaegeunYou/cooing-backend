package com.alpha.kooing.support.repository

import com.alpha.kooing.support.entity.SupportBusinessScrap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface SupportBusinessScrapRepository: JpaRepository<SupportBusinessScrap, Long>{
    @Modifying
    @Query("delete from SupportBusinessScrap s where s.supportBusiness.id=:businessId AND s.user.id=:userId")
    fun deleteByUserIdAndBusinessId(userId: Long, businessId: Long)
}