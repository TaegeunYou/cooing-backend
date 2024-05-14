package com.alpha.kooing.config.auth

import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOauth2UserService(
    val userRepository: UserRepository
):OAuth2UserService<OAuth2UserRequest, OAuth2User>, DefaultOAuth2UserService(){
    //
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User?{
        val oAuth2User:OAuth2User = super.loadUser(userRequest)
        println(oAuth2User)
        val registrationId = userRequest.clientRegistration.registrationId
        val attributes = OAuthAttributes.of(registrationId, oAuth2User.attributes) ?: return null
        val user = userRepository.findByEmail(attributes.email).orElse(null)
        val customOAuth2User=CustomOAuth2User(
            role = Role.LIMITED,
            email = attributes.email,
            username = attributes.name,
            id = -1
        )
        if(user != null){
            customOAuth2User.id = user.id
            customOAuth2User.role = Role.USER
        }
        return customOAuth2User
    }
}