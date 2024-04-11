package com.alpha.kooing.user.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HelloController {
    @GetMapping("/test")
    @ResponseBody
    fun hello():String{
        println("hello")
        return "Hello"
    }
}