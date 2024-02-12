package com.teamsparta.member.global.exception.common

import org.springframework.http.HttpStatus

enum class CommonErrorCode(
    val codeNum: Int,
    val httpStatus: HttpStatus,
    var message: String
): ErrorCode {
    BAD_REQUEST(40000, HttpStatus.BAD_REQUEST, "Bad request"),
    NOT_FOUND(40400, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    INTERNAL_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR,"Internal error"),
    EMAIL_ERROR(40900, HttpStatus.CONFLICT, "The email is already in use");


    override fun errorName(): String = name

    override fun errorNum(): Int = codeNum

    override fun httpStatus(): HttpStatus = httpStatus

    override fun message(): String = message
}

