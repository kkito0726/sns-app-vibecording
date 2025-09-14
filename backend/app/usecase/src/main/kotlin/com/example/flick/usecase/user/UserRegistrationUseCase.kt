package com.example.flick.usecase.user

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.Password
import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.domain.user.specification.UserRegistrationSpecification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class UserRegistrationCommand(
    val username: String,
    val email: String,
    val password: String
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String
)

@Service
class UserRegistrationUseCase(
    private val userRepository: UserRepository,
    private val userRegistrationSpecification: UserRegistrationSpecification
) {

    @Transactional
    fun registerUser(command: UserRegistrationCommand): UserResponse {
        val username = Username(command.username)
        val email = Email(command.email)
        val password = Password(command.password)

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
