package com.example.flick.usecase.flick

import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.domain.like.LikeRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.usecase.flick.response.AuthorInfo
import com.example.flick.usecase.flick.response.FlickDetailResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FlickGetUseCase(
    private val flickRepository: FlickRepository,
    private val userRepository: UserRepository,
    private val likeRepository: LikeRepository
) {
    @Transactional(readOnly = true)
    fun getFlick(flickId: Long, authUserId: Long?): FlickDetailResponse {
        val flick = flickRepository.findById(flickId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_FLICK)

        val author = userRepository.findById(flick.userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        val likeCount = likeRepository.countByFlickId(flickId)
        val isLiked = authUserId?.let { likeRepository.isLiked(it, flickId) } ?: false

        return FlickDetailResponse(
            id = flick.id!!,
            textContent = flick.textContent,
            imageUrl = flick.imageUrl,
            videoUrl = flick.videoUrl,
            postType = flick.postType,
            createdAt = flick.createdAt!!,
            author = AuthorInfo(
                userId = author.id!!,
                username = author.username.value,
                profileImageUrl = author.profileImageUrl
            ),
            likeCount = likeCount,
            isLiked = isLiked
        )
    }
}
