package com.alpha.kooing.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val info = Info().version("1.0").title("OpenAPI").description("API Description")
        val jwtSchemaName = "Authorization"
        val securityRequirement = SecurityRequirement().addList(jwtSchemaName)
        val securityScheme = SecurityScheme()
            .name(jwtSchemaName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT") // JWT 형식을 명확히 설정
            .`in`(SecurityScheme.In.HEADER)

        val components = Components()
            .securitySchemes(mapOf(jwtSchemaName to securityScheme))

        return OpenAPI()
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components)
    }
}
