package com.alpha.kooing.user.controller

import com.alpha.kooing.config.LoginUserManager
import com.alpha.kooing.user.repository.UserRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import kotlin.math.absoluteValue
import kotlin.random.Random

@Controller
class HelloController(
    val userRepository: UserRepository,
    val userManager: LoginUserManager
){
    @GetMapping("/test")
    @ResponseBody
    fun hello(response:HttpServletResponse):Any{
        response.addCookie(Cookie("seed", Random.nextInt().absoluteValue.toString()))
        return "success"
    }

    @GetMapping("/loginuser")
    @ResponseBody
    fun getLoginUser():List<*>{
        val userList = userManager.getLoginUserList()
        if(userList!=null){
            return userList
        }else{
            return mutableListOf<Int>(-1)
        }
    }
}