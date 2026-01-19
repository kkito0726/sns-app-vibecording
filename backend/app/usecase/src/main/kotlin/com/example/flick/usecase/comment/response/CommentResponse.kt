package com.example.flick.usecase.comment.response

import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val userId: Long,
    val flickId: Long,
    val text: String,
    val createdAt: LocalDateTime,
    val authorUsername: String,
    val authorProfileImageUrl: String?
)
