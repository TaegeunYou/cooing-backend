package com.alpha.kooing.config

import JwtAuthenticationFilter
import com.alpha.kooing.config.auth.CustomOAuth2FailureHandler
import com.alpha.kooing.config.auth.CustomOAuth2SuccessHandler
import com.alpha.kooing.config.auth.CustomOauth2UserService
import com.alpha.kooing.config.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import java.util.*


@Configuration
@EnableWebSecurity
class SecurityConfig(
    val customOauth2UserService: CustomOauth2UserService,
    val customOAuth2SuccessHandler: CustomOAuth2SuccessHandler,
    val customOAuth2FailureHandler: CustomOAuth2FailureHandler,
    val jwtTokenProvider: JwtTokenProvider
){
    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        http
            .cors { conf -> conf.configurationSource(object : CorsConfigurationSource{
                override fun getCorsConfiguration(request: HttpServletRequest): CorsConfiguration {
                    val config = CorsConfiguration()
                    config.allowedHeaders = Collections.singletonList("*")
                    config.allowedMethods = Collections.singletonList("*")
                    config.allowedOrigins = Collections.singletonList("http://localhost:3000")
                    config.allowCredentials = true
                    config.maxAge = 3600L
                    config.exposedHeaders = Collections.singletonList("Set-Cookie")
                    config.addExposedHeader("Authentication")
                    return config
                }
            })}

        //csrf disable
        http
            .csrf { auth -> auth.disable() }


        //From 로그인 방식 disable
        http
            .formLogin { auth -> auth.disable() }


        //HTTP Basic 인증 방식 disable
        http
            .httpBasic { auth -> auth.disable() }


        http
            .addFilterAfter(JwtAuthenticationFilter(jwtTokenProvider), OAuth2LoginAuthenticationFilter::class.java)

        //JWTFilter 추가
//        http
//            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        //oauth2
        http
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint { userInfoEndpointConfig ->
                        userInfoEndpointConfig
                            .userService(customOauth2UserService)
                    }
                    .successHandler(customOAuth2SuccessHandler)
                    .failureHandler(customOAuth2FailureHandler)
            }


        //경로별 인가 작업
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/").permitAll()
                    .anyRequest().authenticated()
            }


        //세션 설정 : STATELESS
        http
            .sessionManagement { session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }
}