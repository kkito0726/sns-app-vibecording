package com.example.flick.presentation.flick.request

import com.example.flick.domain.flick.model.PostType
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class CreateFlickRequest(
    val textContent: String?,
    val imageFile: MultipartFile?,
    val videoFile: MultipartFile?,
    @field:NotNull(message = "Post type is required")
    val postType: PostType
)