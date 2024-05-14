package com.alpha.kooing.user.dto

import com.alpha.kooing.user.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable

class CustomOAuth2User(
    var role:Role,
    var username:String,
    var email:String,
    var id:Long?
):OAuth2User{
    override fun getName(): String {
        return username
    }

    override fun getAttributes(): MutableMap<String, Any> {
        val attributes = mutableMapOf<String, Any>()
        attributes["email"] = email
        return attributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val collection:MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { role.name })
        return collection
    }
}