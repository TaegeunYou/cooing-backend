package com.alpha.kooing.config

import JwtAuthenticationFilter
import com.alpha.kooing.config.auth.CustomOAuth2FailureHandler
import com.alpha.kooing.config.auth.CustomOAuth2SuccessHandler
import com.alpha.kooing.config.auth.CustomOauth2UserService
import com.alpha.kooing.config.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
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
    @Value(value = "\${spring.cors.allowed-origin}")
    lateinit var allowedOrigin:String

    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        http
            .cors { conf -> conf.configurationSource(object : CorsConfigurationSource{
                override fun getCorsConfiguration(request: HttpServletRequest): CorsConfiguration {
                    val config = CorsConfiguration()
                    config.addAllowedOrigin(allowedOrigin)
                    config.addAllowedMethod("*")
                    config.addAllowedHeader("*")
                    config.allowCredentials = true
                    config.addExposedHeader("Set-Cookie")
                    config.addExposedHeader("Authorization")
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
                    // 테스트 때문에 임시로 전체 허용 설정
                    .requestMatchers("/**").permitAll()
                    .requestMatchers("/ws/**").permitAll()
                    // 아래부터 실제 경로별 권한 부여 로직
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