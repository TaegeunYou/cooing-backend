//package com.alpha.kooing.config.auth
//
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.core.AuthenticationException
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
//import org.springframework.stereotype.Component
//
//@Component
//class CustomOAuth2FailureHandler : SimpleUrlAuthenticationFailureHandler(){
//    override fun onAuthenticationFailure(
//        request: HttpServletRequest?,
//        response: HttpServletResponse?,
//        exception: AuthenticationException?
//    ) {
//        println("fail to login")
//        println(exception?.message)
//        super.onAuthenticationFailure(request, response, exception)
//    }
//}