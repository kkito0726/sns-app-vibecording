package com.example.flick.domain.error

import org.springframework.http.HttpStatus

data class BusinessLogicException(
    override val errorCode: ErrorCode
) : HttpException(HttpStatus.BAD_REQUEST, errorCode)