package com.alpha.kooing.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsMvcConfig : WebMvcConfigurer {
    @Value(value = "\${spring.cors.allowed-origin}")
    lateinit var allowedOrigin:String

    override fun addCorsMappings(corsRegistry: CorsRegistry) {
        corsRegistry.addMapping("/**")
            .exposedHeaders("Set-Cookie")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowedOrigins(allowedOrigin)
            .allowCredentials(true)

        corsRegistry.addMapping("/ws/**")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowedOrigins(allowedOrigin)
            .allowCredentials(true)
    }
}