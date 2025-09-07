package com.example.flick.domain.user

data class Password(val value: String) {
    init {
        require(value.isNotBlank()) { "Password hash cannot be blank" }
    }
}