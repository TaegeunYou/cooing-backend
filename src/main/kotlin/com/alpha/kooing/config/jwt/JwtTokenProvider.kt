package com.alpha.kooing.config.jwt
import com.alpha.kooing.user.Role
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.math.exp
import kotlin.random.Random
import kotlin.random.nextULong

// accessToken, refreshToken 생성 책임
@Component
@PropertySource("classpath:application.yml")

class JwtTokenProvider(
    @Value("\${spring.jwt.secretKey}") val secret: String
){
    val expiration = 60 * 3600 * 3600L
    val bearerPrefix = "Bearer "
    private final var secretKey:SecretKey
    init{
        secretKey = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)
    }
    fun getJwtEmail(token:String):String{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["email"].toString()
    }
    fun getJwtRole(token: String):String{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["role"].toString()
    }
    fun getJwtUserId(token: String):String{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["id"].toString()
    }
    fun getJwtUsername(token: String):String{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["username"].toString()
    }
    fun isExpired(token: String):Boolean{
        return try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.expiration.before(Date(System.currentTimeMillis()))
        }catch (e:Exception){
            false
        }
    }
    fun createJwt(id:Long?, email:String, role:String, username:String, expiration:Long): String {
        return Jwts.builder()
            .claim("email", email)
            .claim("role", role)
            .claim("id", id.toString())
            .claim("username", username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(secretKey)
            .compact()
    }

    fun resolveToken(request: HttpServletRequest): String {
        var token = request.getHeader("Authorization")?:throw Exception()
        if(token.startsWith(bearerPrefix)){token = token.substring(bearerPrefix.length)}
        return token
    }

    fun createRandomToken():String{
        val randEmail = "test-${Random.nextULong()}"
        return createJwt(
            id = -1,
            email = randEmail,
            role = Role.USER.name,
            username = randEmail,
            expiration = 3600*3600*3600L
        )
    }
}