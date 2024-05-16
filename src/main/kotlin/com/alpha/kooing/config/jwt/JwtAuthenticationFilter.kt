import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.Role
import com.alpha.kooing.user.dto.CustomOAuth2User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter(){
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authorization:String? = null
        val requestUri = request.requestURI
        println(requestUri)
        if(requestUri.matches(regex = Regex("""^/login(?:/.*)?$"""))){
            filterChain.doFilter(request, response)
            return
        }
        if(requestUri.matches(regex = Regex("""^/oauth2(?:/.*)?$"""))){
            filterChain.doFilter(request,response)
            return
        }
        val cookies = request.cookies
        if(cookies == null){
            SecurityContextHolder.getContext().authentication = null
            println("쿠키가 없습니다...")
            filterChain.doFilter(request, response)
            return
        }
        for(cookie in cookies){
            if(cookie.name.equals("Authorization")){
                authorization = cookie.value
            }
        }
        if(authorization == null || jwtTokenProvider.isExpired(authorization)){
            SecurityContextHolder.getContext().authentication = null
            println("유효하지 않은 토큰")
            filterChain.doFilter(request, response)
            return
        }
        println("user role : ${Role.valueOf(jwtTokenProvider.getJwtRole(authorization))}")
        val customOAuth2User = CustomOAuth2User(
            email = jwtTokenProvider.getJwtEmail(authorization),
            role = Role.valueOf(jwtTokenProvider.getJwtRole(authorization)),
            username = "",
            id = jwtTokenProvider.getJwtUserId(authorization).toLong()
        )
        val principal = UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.authorities)
        SecurityContextHolder.getContext().authentication = principal
        println("인증 완료 " + customOAuth2User.authorities)
        filterChain.doFilter(request,response)
    }
}