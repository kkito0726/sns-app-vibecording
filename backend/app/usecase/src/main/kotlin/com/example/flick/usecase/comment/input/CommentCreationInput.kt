package com.example.flick.usecase.comment.input

data class CommentCreationInput(
    val userId: Long,
    val flickId: Long,
    val text: String
)
