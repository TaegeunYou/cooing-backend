package com.alpha.kooing.config.jwt

import com.alpha.kooing.User.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey

// accessToken, refreshToken 생성 책임
@Component
@PropertySource("classpath:application.yml")
class JwtTokenProvider(@Value("\${spring.jwt.secretKey}") secretKey: String) {
    val authKey = "auth"
    val accessTokenExpiration = 1000 * 60 * 60
    val refreshTokenExpiration = 1000 * 60 * 60
    private final var tokenKey:SecretKey

    init {
        val byteSk:ByteArray = Base64.getDecoder().decode(secretKey)
        this.tokenKey = Keys.hmacShaKeyFor(byteSk)
    }

    private fun createAccessToken(pk:Long):String{
        val accessTokenExpirationDate = Date(Date().time + accessTokenExpiration)
        val claims = Jwts.claims(
            mapOf(authKey to Role.USER)
        ).setSubject("$pk")
        val accessToken = Jwts
            .builder()
            .setClaims(claims)
            .setExpiration(accessTokenExpirationDate)
            .signWith(tokenKey, SignatureAlgorithm.HS256)
            .compact()
        return accessToken
    }

    private fun createRefreshToken():String{
        val refreshTokenExpirationDate = Date(Date().time + refreshTokenExpiration)
        val refreshToken = Jwts
            .builder()
            .setExpiration(refreshTokenExpirationDate)
            .compact()
        return refreshToken
    }

    fun validateToken(token:String):Boolean{
        try{
            Jwts
                .parserBuilder()
                .setSigningKey(tokenKey)
                .build()
                .parseClaimsJwt(token)
            return true
        }catch (err:Throwable){
            return false
        }
    }

    fun parseClaims(accessToken:String):Claims{
        try {
            val claims = Jwts
                .parserBuilder()
                .setSigningKey(tokenKey)
                .build()
                .parseClaimsJwt(accessToken)
                .body
            return claims
        }catch (e:ExpiredJwtException){
            return e.claims
        }
    }
}