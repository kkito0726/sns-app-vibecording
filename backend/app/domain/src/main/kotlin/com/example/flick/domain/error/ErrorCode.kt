package com.example.flick.domain.error


enum class ErrorCode(val code: String, val message: String) {
    DUPLICATE_USERNAME("U001", "このユーザー名は既に使用されています。"),
    DUPLICATE_EMAIL("U002", "このメールアドレスは既に使用されています。"),
    INVALID_INPUT("U003", "入力値が不正です。"),
    NOT_FOUND_USER("U004", "ユーザーが見つかりません"),

    NOT_FOUND_FLICK("F001", "Flick投稿が見つかりません"),
    NOT_AUTH_DELETE_FLICK("F002", "このFlick投稿の削除権限がありません")
}

data class ErrorResponse(
    val code: String,
    val message: String
)