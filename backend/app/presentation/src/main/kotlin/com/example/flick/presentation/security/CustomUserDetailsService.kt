package com.example.flick.presentation.security

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(Email(email))
            ?: throw UsernameNotFoundException("User not found with email: $email")
        return User(user.email.value, user.password.value, emptyList())
    }
}