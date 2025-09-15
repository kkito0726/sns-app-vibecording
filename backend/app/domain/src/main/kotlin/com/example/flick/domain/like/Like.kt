package com.example.flick.domain.like

import java.time.LocalDateTime

data class Like(
    val userId: Long,
    val flickId: Long,
    val createdAt: LocalDateTime? = null
)
