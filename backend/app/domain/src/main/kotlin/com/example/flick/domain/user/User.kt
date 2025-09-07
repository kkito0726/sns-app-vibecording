package com.example.flick.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val username: Username,
    val email: Email,
    val password: Password,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

interface UserRepository {
    fun save(user: User): User
    fun findByUsername(username: Username): User?
    fun findByEmail(email: Email): User?
}
