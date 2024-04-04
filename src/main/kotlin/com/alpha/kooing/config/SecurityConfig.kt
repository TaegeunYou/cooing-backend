package com.alpha.kooing.config

import com.alpha.kooing.config.auth.OAuth2LoginSuccessHandler
import com.alpha.kooing.config.jwt.JwtAuthenticationFilter
import com.alpha.kooing.User.Role
import com.alpha.kooing.config.auth.CustomOauth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig{
    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        http
            .csrf { conf->conf.disable() }
            .headers { headersConfig -> headersConfig.frameOptions { frameOptionConf -> frameOptionConf.disable() } }
            // 인증이 필요한 url 설정
            .authorizeHttpRequests { conf ->
                conf.requestMatchers("/api/v1/**").hasRole(Role.USER.name)
            }
            .logout { logoutConf->
                logoutConf.logoutSuccessUrl("/")
            }
            .oauth2Login{oauth2LoginConf->
                oauth2LoginConf.userInfoEndpoint { userInfoEndpointConf->
                    userInfoEndpointConf.userService(CustomOauth2UserService())
                }
                oauth2LoginConf.successHandler(OAuth2LoginSuccessHandler())
            }
            .addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}