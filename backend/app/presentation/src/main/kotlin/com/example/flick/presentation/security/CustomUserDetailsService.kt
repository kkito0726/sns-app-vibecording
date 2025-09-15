package com.example.flick.presentation.security

import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(Username(username))
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return UserDetailsImpl(
            id = user.id!!,
            username = user.username.value,
            pass = user.password.value,
            authorities = emptyList()
        )
    }
}
