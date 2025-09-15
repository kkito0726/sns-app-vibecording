package com.example.flick.domain.error


enum class ErrorCode(val code: String, val message: String) {
    DUPLICATE_USERNAME("U001", "このユーザー名は既に使用されています。"),
    DUPLICATE_EMAIL("U002", "このメールアドレスは既に使用されています。"),
    INVALID_INPUT("U003", "入力値が不正です。"),
    NOT_FOUND_USER("U004", "ユーザーが見つかりません")
}

data class ErrorResponse(
    val code: String,
    val message: String
)