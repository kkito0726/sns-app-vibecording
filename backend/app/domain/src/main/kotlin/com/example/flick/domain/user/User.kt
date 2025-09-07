package com.example.flick.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val username: Username, // Changed from String
    val email: Email,       // Changed from String
    val passwordHash: PasswordHash, // Changed from String
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

interface UserRepository {
    fun save(user: User): User
    fun findByUsername(username: Username): User? // Updated parameter type
    fun findByEmail(email: Email): User?         // Updated parameter type
}
