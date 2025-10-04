package com.jikgwan.common.exception

sealed class JikgwanException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message
) : RuntimeException(message)

class BusinessException(
    errorCode: ErrorCode
) : JikgwanException(errorCode)

class UnauthorizedException(
    errorCode: ErrorCode = ErrorCode.UNAUTHORIZED
) : JikgwanException(errorCode)
