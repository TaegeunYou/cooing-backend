package com.alpha.kooing.user.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/test")
    fun hello():String{
        return "Hello"
    }
}