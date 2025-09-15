package com.example.flick.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val username: Username,
    val email: Email,
    val password: Password,
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
