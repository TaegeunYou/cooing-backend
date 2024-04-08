package com.alpha.kooing.config.auth

import com.alpha.kooing.user.Role
import com.alpha.kooing.user.User
import com.alpha.kooing.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.Collections

@Service
class CustomOauth2UserService(
    val userRepository: UserRepository
):OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val service:OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService()
        val oAuth2User:OAuth2User = service.loadUser(userRequest)
        val registrationId:String = userRequest.clientRegistration.registrationId
        val attributes:OAuthAttributes = OAuthAttributes.of(registrationId, oAuth2User.attributes)
        saveOrUpdate(attributes)
        val authority =Collections.singleton(SimpleGrantedAuthority(Role.USER.name))
        return OAuth2CustomUser(attributes, authority)
    }

    fun saveOrUpdate(attributes: OAuthAttributes):User{
        val user = userRepository.findByEmail(attributes.email)
            .orElse(User(attributes.email))
        return userRepository.save(user)
    }
}