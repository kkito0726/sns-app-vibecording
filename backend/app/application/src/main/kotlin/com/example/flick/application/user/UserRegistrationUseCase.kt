package com.example.flick.application.user

import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class UserRegistrationCommand(
    val username: String,
    val email: String,
    val password: String
)

data class UserDto(
    val id: Long,
    val username: String,
    val email: String
)

@Service
open class UserRegistrationUseCase(
    private val userRepository: UserRepository
) {

    @Transactional
    fun registerUser(command: UserRegistrationCommand): UserDto {
        // Check if username or email already exists
        if (userRepository.findByUsername(command.username) != null) {
            throw IllegalArgumentException("Username already exists")
        }
        if (userRepository.findByEmail(command.email) != null) {
            throw IllegalArgumentException("Email already exists")
        }

        val newUser = User(
            username = command.username,
            email = command.email,
            passwordHash = command.password // passwordHash is actually raw password here, will be hashed in infra layer
        )
        val savedUser = userRepository.save(newUser)

        return UserDto(
            id = savedUser.id!!,
            username = savedUser.username,
            email = savedUser.email
        )
    }
}
