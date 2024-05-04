package com.alpha.kooing.config.auth

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
// controller로 들어온 요청의 cors 설정
class CorsMvcConfig : WebMvcConfigurer{
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowedOrigins("http://localhost:3000")
            .allowCredentials(true)
            .exposedHeaders("Set-Cookie")
    }
}