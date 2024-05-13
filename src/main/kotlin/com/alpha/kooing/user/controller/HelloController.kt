package com.alpha.kooing.user.controller

import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.entity.User
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HelloController(
    val userRepository: UserRepository
){
    @GetMapping("/test")
    @ResponseBody
    fun hello():MutableList<User>{
        val currentUser = SecurityContextHolder.getContext().authentication.principal as CustomOAuth2User?:null
        println(currentUser?.email)
        return userRepository.findAll()
    }
}