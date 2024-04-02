package com.alpha.kooing.service

import com.alpha.kooing.config.auth.OAuth2CustomUser
import com.alpha.kooing.config.auth.OAuthAttributes
import com.alpha.kooing.domain.user.Role
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.Collections

@Service
class CustomOauth2UserService:OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val service:OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService()
        val oAuth2User:OAuth2User = service.loadUser(userRequest)
        val registrationId:String = userRequest.clientRegistration.registrationId
        val attributes:OAuthAttributes = OAuthAttributes.of(registrationId, oAuth2User.attributes)
        saveOrUpdate(attributes)
        val authority =Collections.singleton(SimpleGrantedAuthority(Role.USER.name))
        return OAuth2CustomUser(attributes, authority)
    }

    fun saveOrUpdate(attributes: OAuthAttributes){

    }
}