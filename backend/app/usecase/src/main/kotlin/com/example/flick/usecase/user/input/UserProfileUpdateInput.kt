package com.example.flick.usecase.user.input

import org.springframework.web.multipart.MultipartFile

data class UserProfileUpdateInput(
    val userId: Long,
    val username: String?,
    val bio: String?,
    val profileImage: MultipartFile?
)
