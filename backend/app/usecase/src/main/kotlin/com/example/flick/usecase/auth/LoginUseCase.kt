package com.example.flick.usecase.auth

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.UserRepository
import com.example.flick.usecase.config.TokenProvider
import com.example.flick.usecase.auth.input.LoginInput
import com.example.flick.usecase.auth.response.LoginResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) {
    fun execute(request: LoginInput): LoginResponse {
        val user = userRepository.findByEmail(Email(request.email))
            ?: throw IllegalArgumentException("User not found")

        if (!passwordEncoder.matches(request.password, user.password.value)) {
            throw IllegalArgumentException("Invalid password")
        }

        val token = tokenProvider.createToken(user.username.value)
        return LoginResponse(token)
    }
}
