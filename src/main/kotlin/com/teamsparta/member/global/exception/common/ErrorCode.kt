package com.teamsparta.member.global.exception.common

import org.springframework.http.HttpStatus

interface ErrorCode {

    fun errorName(): String

    fun errorNum(): Int

    fun httpStatus(): HttpStatus

    fun message(): String
}
