package com.teamsparta.member.global.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamsparta.member.global.exception.auth.AuthErrorCode
import com.teamsparta.member.global.exception.common.ErrorCode
import com.teamsparta.member.global.exception.common.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler: AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val errorCode = AuthErrorCode.PERMISSION_DENIED

        setResponse(response, errorCode)
    }
    private fun setResponse(response: HttpServletResponse, code: AuthErrorCode) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        val objectMapper = ObjectMapper()
        val jsonString = objectMapper.writeValueAsString(ErrorResponse.of(code))
        response.writer.write(jsonString)
    }
}