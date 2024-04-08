package com.alpha.kooing.config.jwt

import com.alpha.kooing.user.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.Arrays
import java.util.Base64
import java.util.Date
import java.util.stream.Collectors
import javax.crypto.SecretKey

// accessToken, refreshToken 생성 책임
@Component
@PropertySource("classpath:application.yml")
class JwtTokenProvider{
    @Value("\${spring.jwt.secretKey}")
    private lateinit var secretKey: String

    private val authKey = "auth"
    private val accessTokenExpiration = 1000 * 60 * 60
    private val refreshTokenExpiration = 1000 * 60 * 60
    private final var tokenKey:SecretKey

    init {
        val byteSk:ByteArray = Base64.getDecoder().decode(secretKey)
        this.tokenKey = Keys.hmacShaKeyFor(byteSk)
    }

    private fun createAccessToken(pk:Long):String{
        val accessTokenExpirationDate = Date(Date().time + accessTokenExpiration)
        // claim( token 정보 ) 생성
        val claims = Jwts
            .claims()
            .setSubject("$pk")
        claims[authKey] = Role.USER
        // jwt token 생성
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

    // 토큰을 통한 인증 + claim 정보 획득
    fun getAuthentication(accessToken: String):Authentication{
        val claims = parseClaims(accessToken)
        if (claims[authKey] == null){
            throw RuntimeException("잘못된 access token")
        }
        val pk = Integer.parseInt(claims.subject)
        val authorities = Arrays.stream(arrayOf(claims[authKey].toString()))
            .map { SimpleGrantedAuthority(it) }
            .collect(Collectors.toList())
        return UsernamePasswordAuthenticationToken(pk, "", authorities)
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