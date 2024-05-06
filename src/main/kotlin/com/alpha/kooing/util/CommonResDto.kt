package com.alpha.kooing.util

data class CommonResDto<T>(
    val resultCode:T,
    val resultMsg:String,
    val data:T?
)