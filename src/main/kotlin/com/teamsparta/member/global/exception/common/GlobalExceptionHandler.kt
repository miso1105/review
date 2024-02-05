package com.teamsparta.member.global.exception.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        val errorCode = CommonErrorCode.INTERNAL_ERROR

        return ResponseEntity.status(errorCode.httpStatus()).body(ErrorResponse.of(errorCode))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body((e.bindingResult.allErrors.map { it.defaultMessage }.reduce{ acc, cur -> "$acc, $cur"}))
    }


    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmailException(e: DuplicateEmailException): ResponseEntity<ErrorResponse> {
        val errorCode = CommonErrorCode.EMAIL_ERROR

        return ResponseEntity.status(errorCode.httpStatus()).body(ErrorResponse.of(errorCode))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorCode = CommonErrorCode.BAD_REQUEST
        return ResponseEntity.status(errorCode.httpStatus()).body(ErrorResponse.of(errorCode))
    }

    @ExceptionHandler(NoSuchEntityException::class)
    fun handleNoSuchEntityException(e: NoSuchEntityException): ResponseEntity<ErrorResponse> {
        val errorCode = CommonErrorCode.NOT_FOUND
        return ResponseEntity.status(errorCode.httpStatus()).body(ErrorResponse.of(errorCode))
    }
}
