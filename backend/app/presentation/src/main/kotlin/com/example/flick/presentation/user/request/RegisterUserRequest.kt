package com.example.flick.presentation.user.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterUserRequest(
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String,

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    val email: String,

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    val password: String
)