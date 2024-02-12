package com.teamsparta.member.global.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamsparta.member.global.exception.auth.AuthErrorCode
import com.teamsparta.member.global.exception.common.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val code: AuthErrorCode? = request?.getAttribute("exception") as AuthErrorCode?

        code?.let {
            when (code) {
                AuthErrorCode.EXPIRED_ACCESS_TOKEN -> setResponse(response!!, code)
                else -> setResponse(response!!, AuthErrorCode.COMMON_UNAUTHORIZED)
            }
        }
    }
    private fun setResponse(response: HttpServletResponse, code: AuthErrorCode) {

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        val objectMapper = ObjectMapper()
        val jsonString = objectMapper.writeValueAsString(ErrorResponse.of(code))
        response.writer.write(jsonString)
    }
}