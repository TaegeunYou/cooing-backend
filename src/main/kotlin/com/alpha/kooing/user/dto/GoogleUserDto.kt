package com.alpha.kooing.user.dto

class GoogleUserDto(
    val email:String,
    val name:String,
){
    override fun toString(): String {
        return "GoogleUserDto(email='$email', name='$name')"
    }
}