package com.example.flick.usecase.flick

import com.example.flick.domain.common.FileStorageService
import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.flick.Flick
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.usecase.flick.input.FlickCreationInput
import com.example.flick.usecase.flick.response.FlickResponse
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class FlickCreationUseCase(
    private val flickRepository: FlickRepository,
    private val userRepository: UserRepository,
    private val fileStorageService: FileStorageService
) {
     fun createFlick(input: FlickCreationInput, authUser: UserDetails): FlickResponse {
         val authUserId = userRepository.findByUsername(Username(authUser.username))?.id
             ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        var imageUrl: String? = null
        if (input.imageFile != null && !input.imageFile.isEmpty) {
            imageUrl = fileStorageService.uploadFile(input.imageFile)
        }

        var videoUrl: String? = null
        if (input.videoFile != null && !input.videoFile.isEmpty) {
            videoUrl = fileStorageService.uploadFile(input.videoFile)
        }

        val flick = Flick(
            userId = authUserId,
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