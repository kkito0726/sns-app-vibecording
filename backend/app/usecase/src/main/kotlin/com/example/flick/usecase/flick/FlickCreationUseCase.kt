package com.example.flick.usecase.flick

import com.example.flick.domain.common.FileStorageService
import com.example.flick.domain.flick.Flick
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.domain.user.Email
import com.example.flick.domain.user.UserRepository
import com.example.flick.usecase.flick.input.FlickCreationInput
import com.example.flick.usecase.flick.response.FlickResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class FlickCreationUseCase(
    private val flickRepository: FlickRepository,
    private val userRepository: UserRepository,
    private val fileStorageService: FileStorageService
) {
    fun createFlick(input: FlickCreationInput): FlickResponse {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val userEmail = if (principal is UserDetails) {
            principal.username
        } else {
            throw IllegalStateException("User not authenticated")
        }

        val user = userRepository.findByEmail(Email(userEmail))
            ?: throw IllegalStateException("Authenticated user not found in repository")

        var imageUrl: String? = null
        if (input.imageFile != null && !input.imageFile.isEmpty) {
            imageUrl = fileStorageService.uploadFile(input.imageFile)
        }

        var videoUrl: String? = null
        if (input.videoFile != null && !input.videoFile.isEmpty) {
            videoUrl = fileStorageService.uploadFile(input.videoFile)
        }

        val flick = Flick(
            userId = user.id!!,
            textContent = input.textContent,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            postType = input.postType
        )

        val savedFlick = flickRepository.save(flick)

        return FlickResponse(
            id = savedFlick.id!!,
            userId = savedFlick.userId,
            textContent = savedFlick.textContent,
            imageUrl = savedFlick.imageUrl,
            videoUrl = savedFlick.videoUrl,
            postType = savedFlick.postType,
            createdAt = savedFlick.createdAt!!
        )
    }
}