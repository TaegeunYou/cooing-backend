package com.alpha.kooing.config.auth

import com.alpha.kooing.user.Role
import com.alpha.kooing.user.User
import com.alpha.kooing.user.dto.CustomOAuth2User
import com.alpha.kooing.user.enum.RoleType
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
):OAuth2UserService<OAuth2UserRequest, OAuth2User>, DefaultOAuth2UserService(){
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User?{
        val oAuth2User:OAuth2User = super.loadUser(userRequest)
        println(oAuth2User)
        val registrationId = userRequest.clientRegistration.registrationId
        val attributes = OAuthAttributes.of(registrationId, oAuth2User.attributes) ?: return null
        // null이면 에러를 던지니까 널 체크가 의미가 없음 => 에러를 catch하거나 orElse 사용
        val existUser = userRepository.findByEmail(attributes.email)
            .orElse(User(email = attributes.email, username = attributes.name, role = Role.USER, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), RoleType.헨젤, "", null, true))
        existUser.username = attributes.name
        userRepository.save(existUser)
        val customOAuth2User = CustomOAuth2User(role = Role.USER, email = attributes.email, username = attributes.name)
        return customOAuth2User
    }
}