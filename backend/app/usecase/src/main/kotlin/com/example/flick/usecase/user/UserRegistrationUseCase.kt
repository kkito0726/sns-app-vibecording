package com.example.flick.usecase.user

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.Password
import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.domain.user.specification.UserRegistrationSpecification
import com.example.flick.usecase.user.input.UserRegistrationInput
import com.example.flick.usecase.user.response.UserResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserRegistrationUseCase(
    private val userRepository: UserRepository,
    private val userRegistrationSpecification: UserRegistrationSpecification,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun registerUser(input: UserRegistrationInput): UserResponse {
        val username = Username(input.username)
        val email = Email(input.email)
        val password = Password(passwordEncoder.encode(input.password))

        // 重複チェック
        userRegistrationSpecification.checkUsernameUniqueness(username)
        userRegistrationSpecification.checkEmailUniqueness(email)

        val newUser = User(
            username = username,
            email = email,
            password = password
        )
        val savedUser = userRepository.save(newUser)

        return UserResponse(
            id = savedUser.id!!,
            username = savedUser.username.value,
            email = savedUser.email.value
        )
    }
}
