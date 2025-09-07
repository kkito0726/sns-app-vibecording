package com.example.flick.presentation.user

import com.example.flick.application.user.UserDto
import com.example.flick.application.user.UserRegistrationCommand
import com.example.flick.application.user.UserRegistrationUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class RegisterUserRequest(
    @field:NotBlank(message = "Username cannot be empty")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String,

    @field:NotBlank(message = "Email cannot be empty")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password cannot be empty")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    val password: String
)

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRegistrationUseCase: UserRegistrationUseCase
) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: RegisterUserRequest): ResponseEntity<UserDto> {
        val command = UserRegistrationCommand(
            username = request.username,
            email = request.email,
            password = request.password
        )
        val userDto = userRegistrationUseCase.registerUser(command)
        return ResponseEntity(userDto, HttpStatus.CREATED)
    }
}
