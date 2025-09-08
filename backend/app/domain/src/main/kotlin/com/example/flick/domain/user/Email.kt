package com.example.flick.domain.user

import com.example.flick.domain.error.BusinessLogicException
import com.example.flick.domain.error.ErrorCode

data class Email(val value: String) {
    init {
        require(value.isNotBlank()) { throw BusinessLogicException(ErrorCode.INVALID_INPUT) }
        require(value.contains("@")) { throw BusinessLogicException(ErrorCode.INVALID_INPUT) }
        // More robust email validation can be added here if needed, e.g., using regex
    }
}