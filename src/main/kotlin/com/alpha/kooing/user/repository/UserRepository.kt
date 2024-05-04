package com.alpha.kooing.user.repository

import com.alpha.kooing.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository:JpaRepository<User, Long>{
    @Override
    fun findByEmail(email:String):Optional<User>
}