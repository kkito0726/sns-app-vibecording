package com.example.flick.usecase.flick.response

import com.example.flick.domain.flick.model.PostType
import java.time.LocalDateTime

data class FlickDetailResponse(
    val id: Long,
    val textContent: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val postType: PostType,
    val createdAt: LocalDateTime,
    val author: AuthorInfo,
    val likeCount: Int,
    val isLiked: Boolean
)

data class AuthorInfo(
    val userId: Long,
    val username: String,
    val profileImageUrl: String?
)
