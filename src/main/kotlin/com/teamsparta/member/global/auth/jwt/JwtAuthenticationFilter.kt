package com.teamsparta.member.global.auth.jwt

import com.teamsparta.member.global.auth.UserPrincipal
import com.teamsparta.member.global.exception.auth.AuthErrorCode
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtPlugin: JwtPlugin
): OncePerRequestFilter() {

    companion object {
        private val BEARER_PATTERN = Regex("^Bearer (.+?)$")
    }

    // HttpServletRequest 에서 토큰 바로 가져와
    private fun HttpServletRequest.getBearerToken(): String? {
        val headerValue = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return BEARER_PATTERN.find(headerValue)?.groupValues?.get(1)
    }


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 엑세스 토큰만
        val jwt = request.getBearerToken()

        if (jwt != null) {
            jwtPlugin.validateToken(jwt)
                .onSuccess {
                    val userId = it.payload.subject.toLong()
                    val nickName = it.payload.get("nickName", String::class.java)
                    val role = it.payload.get("role", String::class.java)
                    val email = it.payload.get("email", String::class.java)

                    val principal = UserPrincipal(
                        id = userId,
                        nickName = nickName,
                        email = email,
                        role = setOf(role)
                    )

                    val authentication = JwtAuthenticationToken(
                        principal = principal,
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    )
                    SecurityContextHolder.getContext().authentication = authentication // 현재 사용자의 인증정보
                }.onFailure { exception ->  // 실패한 예외 객체를 받는 매개변수, 이 예외는 엔트리포인트로
                    when (exception) {  // HttpServletRequest 의 속성(attribute)에 설정(String, Object)
                        is ExpiredJwtException -> request.setAttribute("exception", AuthErrorCode.EXPIRED_ACCESS_TOKEN)
                        else -> request.setAttribute("exception", AuthErrorCode.COMMON_UNAUTHORIZED)
                    }
                }
        }
        filterChain.doFilter(request, response)
    }
}
