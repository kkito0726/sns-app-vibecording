package com.example.flick.presentation.user

import com.example.flick.presentation.user.request.LoginUserRequest
import com.example.flick.presentation.user.request.RegisterUserRequest
import com.example.flick.usecase.user.UserRegistrationUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.flick.usecase.user.UserLoginUseCase
import com.example.flick.usecase.user.input.UserLoginInput
import com.example.flick.usecase.user.input.UserRegistrationInput
import com.example.flick.usecase.user.response.UserLoginResponse
import com.example.flick.usecase.user.response.UserResponse

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRegistrationUseCase: UserRegistrationUseCase,
    private val userLoginUseCase: UserLoginUseCase
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginUserRequest): ResponseEntity<UserLoginResponse> {
        val useCaseRequest = UserLoginInput(request.email, request.password)
        return ResponseEntity.ok(userLoginUseCase.execute(useCaseRequest))
    }

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: RegisterUserRequest): ResponseEntity<UserResponse> {
        val input = UserRegistrationInput(
            username = request.username,
            email = request.email,
            password = request.password
        )
        return ResponseEntity(
            userRegistrationUseCase.registerUser(input),
            HttpStatus.OK
        )
    }
}
