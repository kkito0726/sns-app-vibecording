package com.example.flick.presentation.flick

import com.example.flick.domain.flick.model.PostType
import com.example.flick.usecase.flick.FlickCreationUseCase
import com.example.flick.usecase.flick.input.FlickCreationInput
import com.example.flick.usecase.flick.response.FlickResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/flicks")
class FlickController(
    private val flickCreationUseCase: FlickCreationUseCase
) {
    @PostMapping
    fun createFlick(
        @RequestParam("postType") postType: PostType,
        @RequestParam(value = "textContent", required = false) textContent: String?,
        @RequestPart(value = "imageFile", required = false) imageFile: MultipartFile?,
        @RequestPart(value = "videoFile", required = false) videoFile: MultipartFile?
    ): ResponseEntity<FlickResponse> {
        val input = FlickCreationInput(
            textContent = textContent,
            imageFile = imageFile,
            videoFile = videoFile,
            postType = postType
        )
        return ResponseEntity(
            flickCreationUseCase.createFlick(input),
            HttpStatus.CREATED
        )
    }
}