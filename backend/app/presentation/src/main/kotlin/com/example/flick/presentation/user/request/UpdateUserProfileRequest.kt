package com.example.flick.presentation.user.request

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateUserProfileRequest(
    @Schema(description = "新しいユーザー名")
    val username: String?,
    @Schema(description = "新しい自己紹介文")
    val bio: String?
)
