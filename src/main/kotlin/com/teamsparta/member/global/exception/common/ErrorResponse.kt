package com.teamsparta.member.global.exception.common

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ErrorResponse (
    val codeNum: Int,
    val errorName: String,
    val message: String,
) {
    companion object {
        fun of(errorCode: ErrorCode) = ErrorResponse(errorCode.errorNum(), errorCode.errorName(), errorCode.message())
    }
}