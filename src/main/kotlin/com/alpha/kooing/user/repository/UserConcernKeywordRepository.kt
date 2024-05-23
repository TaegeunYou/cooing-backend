package com.alpha.kooing.user.repository

import com.alpha.kooing.user.User
import com.alpha.kooing.user.entity.UserConcernKeyword
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserConcernKeywordRepository : JpaRepository<UserConcernKeyword, Long>
