package com.example.flick.domain.user

data class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(value.contains("@")) { "Invalid email format" }
        // More robust email validation can be added here if needed, e.g., using regex
    }
}