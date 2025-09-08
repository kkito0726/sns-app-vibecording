package com.example.flick.domain.user.specification

import com.example.flick.domain.error.ConflictException
import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.user.Email
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import org.springframework.stereotype.Component // Use @Component for domain specifications if they are Spring-managed

@Component
class UserRegistrationSpecification(
    private val userRepository: UserRepository
) {
    fun checkUsernameUniqueness(username: Username) {
        if (userRepository.findByUsername(username) != null) {
            throw ConflictException(ErrorCode.DUPLICATE_USERNAME)
        }
    }

    fun checkEmailUniqueness(email: Email) {
        if (userRepository.findByEmail(email) != null) {
            throw ConflictException(ErrorCode.DUPLICATE_EMAIL)
        }
    }
}