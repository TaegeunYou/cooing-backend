package com.alpha.kooing.config

import com.alpha.kooing.config.auth.CustomOAuth2SuccessHandler
import com.alpha.kooing.config.jwt.JwtAuthenticationFilter
import com.alpha.kooing.user.Role
import com.alpha.kooing.config.auth.CustomOauth2UserService
import org.apache.tomcat.util.file.ConfigurationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val customOauth2UserService: CustomOauth2UserService,
    val jwtAuthenticationFilter: JwtAuthenticationFilter,
    val customOAuth2SuccessHandler: CustomOAuth2SuccessHandler,
    val securityCorsConfig: SecurityCorsConfig
){
    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        http
            .cors {conf -> conf.configurationSource(securityCorsConfig)}
            .csrf { conf->conf.disable() }
            .formLogin{conf -> conf.disable()}
            .rememberMe{conf -> conf.disable()}
            .httpBasic { conf -> conf.disable() }
            .sessionManagement{conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .headers { headersConfig -> headersConfig.frameOptions { frameOptionConf -> frameOptionConf.disable() } }
            // 인증이 필요한 url 설정
            .authorizeHttpRequests { conf ->
                // 로그인 요청 url 승인
                conf.requestMatchers("/oauth2/**").permitAll()
            }
            .logout { logoutConf->
                logoutConf.logoutSuccessUrl("/")
            }
            .oauth2Login{oauth2LoginConf->
                oauth2LoginConf.authorizationEndpoint{ endpointConf->
                    // 소셜 로그인 url => 클라이언트에서 여기에 요청을 보내면 됨
                    endpointConf.baseUri("/oauth2/authorize")
                }
                oauth2LoginConf.userInfoEndpoint { userInfoEndpointConf->
                    userInfoEndpointConf.userService(customOauth2UserService)
                }
                oauth2LoginConf.successHandler(customOAuth2SuccessHandler)
            }

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}