package com.alpha.kooing.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.util.TimeZone

@Configuration
class TimeZone {
    @PostConstruct
    fun setTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }
}