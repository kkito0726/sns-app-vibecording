package com.example.flick.presentation.flick.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CommentCreationRequest(
    @field:NotBlank(message = "コメントは必須です")
    @field:Size(max = 1000, message = "コメントは1000文字以内で入力してください")
    @Schema(description = "コメント本文", example = "素晴らしい投稿ですね！")
    val text: String
)
