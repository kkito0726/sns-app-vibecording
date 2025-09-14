package com.example.flick.domain.error

import org.springframework.http.HttpStatus

open class HttpException(
    open val httpStatus: HttpStatus,
    open val errorCode: ErrorCode,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)