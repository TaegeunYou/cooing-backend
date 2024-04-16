package com.alpha.kooing.config.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable

class OAuth2CustomUser(
    val attributes: OAuthAttributes,
    val authority: MutableCollection<out GrantedAuthority>
):OAuth2User, Serializable
{
    override fun getName(): String {
        return attributes.name
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return attributes.attributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authority
    }
}