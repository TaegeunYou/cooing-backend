package com.alpha.kooing.user.entity

import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.UserResponseDto
import org.aspectj.lang.annotation.Before
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll

class UserTest {
    companion object{
        var user:User? = null

        @BeforeAll
        @JvmStatic
        internal fun initData(){
            user = User(
                email = "email",
                username = "username",
                role = Role.USER
            )
        }
    }

    @Test
    fun toResponseDto() {
        val userResDto = user?.toResponseDto()
        Assertions.assertThat(userResDto?.email).isEqualTo(userResDto?.email)
    }
}