package com.alpha.kooing.user.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@RestController
class UserController{
    @GetMapping("/user/google/login")
    fun googleLogin(){
        val restTemplate = RestTemplate()
    }
}