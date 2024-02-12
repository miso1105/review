package com.teamsparta.member.global.exception.auth

import com.teamsparta.member.global.exception.common.ErrorCode
import org.springframework.http.HttpStatus

enum class AuthErrorCode(
    val codeNum: Int,
    val httpStatus: HttpStatus,
    val message: String
): ErrorCode {
    EXPIRED_ACCESS_TOKEN(40103, HttpStatus.UNAUTHORIZED, "Access token expired"),
    COMMON_UNAUTHORIZED(40100, HttpStatus.UNAUTHORIZED, "Unauthorized"),
    PERMISSION_DENIED(40300, HttpStatus.FORBIDDEN,"Forbidden");

    override fun errorName() = name

    override fun errorNum() = codeNum

    override fun httpStatus() = httpStatus

    override fun message() = message

}
