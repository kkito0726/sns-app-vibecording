package com.example.flick.domain.follow

import java.time.LocalDateTime

data class Follow(
    val followerId: Long,
    val followingId: Long,
    val createdAt: LocalDateTime = LocalDateTime.now()
)