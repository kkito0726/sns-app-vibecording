package com.example.flick.presentation.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginUserRequest(
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    val email: String,

    @NotBlank(message = "Password cannot be empty")
    val password: String
)