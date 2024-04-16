package com.alpha.kooing.user.controller

import com.alpha.kooing.user.User
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(
    val userRepository: UserRepository
){
    @GetMapping("/test")
    fun hello():List<User>{
        println("hello")
        return userRepository.findAll()
    }
}