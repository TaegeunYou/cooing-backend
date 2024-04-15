package com.alpha.kooing.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import java.util.*

@Component
class SecurityCorsConfig : CorsConfigurationSource{
    override fun getCorsConfiguration(request: HttpServletRequest): CorsConfiguration? {
        val conf = CorsConfiguration()
        conf.addAllowedOrigin("/*")
        conf.addAllowedHeader("*")
        conf.addAllowedMethod("*")
        conf.addExposedHeader("Set-Cookie")
        conf.addExposedHeader("Authorization")
        return conf
    }
}
