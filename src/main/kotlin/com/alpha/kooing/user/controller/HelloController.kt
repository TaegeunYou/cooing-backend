package com.alpha.kooing.user.controller

import com.alpha.kooing.user.User
import com.alpha.kooing.user.repository.UserRepository
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
        return userRepository.findAll()
    }
}