package com.example.flick.domain.comment

import java.time.LocalDateTime

data class Comment(
    val id: Long? = null,
    val userId: Long,
    val flickId: Long,
    val text: String,
    val createdAt: LocalDateTime? = null
)
