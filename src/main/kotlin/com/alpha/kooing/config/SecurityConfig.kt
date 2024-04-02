package com.alpha.kooing.config

import com.alpha.kooing.domain.user.Role
import com.alpha.kooing.service.CustomOauth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val customOauth2UserService: CustomOauth2UserService
){
    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        http
            .csrf { conf->conf.disable() }
            .headers { headersConfig -> headersConfig.frameOptions { frameOptionConf -> frameOptionConf.disable() } }
            .authorizeHttpRequests { conf ->
                conf.requestMatchers("/api/v1/**").hasRole(Role.USER.name)
            }
            .logout { logoutConf->
                logoutConf.logoutSuccessUrl("/")
            }
            .oauth2Login{oauth2LoginConf->
                oauth2LoginConf.userInfoEndpoint { userInfoEndpointConf->
                    userInfoEndpointConf.userService(customOauth2UserService)
                }
            }
        return http.build()
    }
}