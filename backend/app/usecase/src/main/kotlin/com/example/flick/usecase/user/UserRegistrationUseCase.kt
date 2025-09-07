package com.example.flick.usecase.user

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.PasswordHash
import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.domain.user.specification.UserRegistrationSpecification // Import the new specification
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
    private val userRepository: UserRepository,
    private val userRegistrationSpecification: UserRegistrationSpecification // Inject the specification
) {

    @Transactional
    fun registerUser(command: UserRegistrationCommand): UserDto {
        val username = Username(command.username)
        val email = Email(command.email)
        val passwordHash = PasswordHash(command.password)

        // Use the specification for uniqueness checks
        userRegistrationSpecification.checkUsernameUniqueness(username)
        userRegistrationSpecification.checkEmailUniqueness(email)

        val newUser = User(
            username = username,
            email = email,
            passwordHash = passwordHash
        )
        val savedUser = userRepository.save(newUser)

        return UserDto(
            id = savedUser.id!!,
            username = savedUser.username.value,
            email = savedUser.email.value
        )
    }
}
