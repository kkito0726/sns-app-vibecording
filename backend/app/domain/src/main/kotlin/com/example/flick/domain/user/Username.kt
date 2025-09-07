package com.example.flick.domain.user

data class Username(val value: String) {
    init {
        require(value.isNotBlank()) { "Username cannot be blank" }
        require(value.length in 3..50) { "Username must be between 3 and 50 characters" }
    }
}