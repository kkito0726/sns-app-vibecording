package com.example.flick.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

interface UserRepository {
    fun save(user: User): User
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
}
