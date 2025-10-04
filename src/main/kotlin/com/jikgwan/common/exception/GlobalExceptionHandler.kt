package com.jikgwan.common.exception

import com.jikgwan.adapter.`in`.web.common.ApiResponse
import com.jikgwan.adapter.`in`.web.common.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        ex: BusinessException
    ): ResponseEntity<ApiResponse<Unit>> {
        logger.warn("Business exception: ${ex.errorCode}", ex)

        val response = ApiResponse.error<Unit>(
            ErrorResponse(
                errorCode = ex.errorCode.code,
                message = ex.message
            )
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(
        ex: UnauthorizedException
    ): ResponseEntity<ApiResponse<Unit>> {
        logger.warn("Unauthorized exception: ${ex.errorCode}", ex)

        val response = ApiResponse.error<Unit>(
            ErrorResponse(
                errorCode = ex.errorCode.code,
                message = ex.message
            )
        )

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ApiResponse<Unit>> {
        val fieldErrors = ex.bindingResult.fieldErrors
            .associate { it.field to (it.defaultMessage ?: "Invalid value") }

        val response = ApiResponse.error<Unit>(
            ErrorResponse(
                errorCode = "VALIDATION_ERROR",
                message = "입력값이 올바르지 않습니다",
                fieldErrors = fieldErrors
            )
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("Unexpected exception", ex)

        val response = ApiResponse.error<Unit>(
            ErrorResponse(
                errorCode = "INTERNAL_SERVER_ERROR",
                message = "서버 내부 오류가 발생했습니다"
            )
        )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response)
    }
}
