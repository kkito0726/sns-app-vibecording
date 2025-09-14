package com.example.flick.usecase.flick.response

import com.example.flick.domain.flick.model.PostType
import java.time.LocalDateTime

data class FlickResponse(
    val id: Long,
    val userId: Long,
    val textContent: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val postType: PostType,
    val createdAt: LocalDateTime
)