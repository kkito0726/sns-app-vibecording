package com.example.flick.domain.user

data class PasswordHash(val value: String) {
    init {
        require(value.isNotBlank()) { "Password hash cannot be blank" }
    }
}