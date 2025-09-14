package com.example.flick.usecase.flick.input

import com.example.flick.domain.flick.model.PostType
import org.springframework.web.multipart.MultipartFile

data class FlickCreationInput(
    val textContent: String?,
    val imageFile: MultipartFile?,
    val videoFile: MultipartFile?,
    val postType: PostType
)