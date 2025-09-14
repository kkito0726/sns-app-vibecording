package com.example.flick.domain.flick

import com.example.flick.domain.flick.model.PostType
import java.time.LocalDateTime

data class Flick(
    val id: Long? = null,
    val userId: Long,
    val textContent: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val postType: PostType,
    val createdAt: LocalDateTime? = null
)
