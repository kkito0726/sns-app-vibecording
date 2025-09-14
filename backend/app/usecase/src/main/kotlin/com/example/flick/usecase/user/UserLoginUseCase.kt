package com.example.flick.usecase.user

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.UserRepository
import com.example.flick.usecase.config.TokenProvider
import com.example.flick.usecase.user.input.UserLoginInput
import com.example.flick.usecase.user.response.UserLoginResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserLoginUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) {
    fun execute(request: UserLoginInput): UserLoginResponse {
        val user = userRepository.findByEmail(Email(request.email))
            ?: throw IllegalArgumentException("User not found")

        if (!passwordEncoder.matches(request.password, user.password.value)) {
            throw IllegalArgumentException("Invalid password")
        }

        val token = tokenProvider.createToken(user.username.value)
        return UserLoginResponse(token)
    }
}
