package com.alpha.kooing.user.controller

import com.alpha.kooing.user.service.UserService
import com.alpha.kooing.util.CommonResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService
){
    @GetMapping("")
    fun findAllUsers() : CommonResDto<*>{
        val result = userService.findAll() ?: return CommonResDto(HttpStatus.BAD_REQUEST, "Bad Request", null)
        return CommonResDto(HttpStatus.OK, "OK", result)
    }

    @GetMapping("/match")
    fun findSimilarInterest(@RequestParam hobbies : List<String>) : CommonResDto<*>{
        val users = userService.findMatchingUser()
        return CommonResDto(HttpStatus.OK, "OK", users)
    }
}