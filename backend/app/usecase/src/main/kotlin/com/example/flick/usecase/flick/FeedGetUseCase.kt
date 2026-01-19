package com.example.flick.usecase.flick

import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.domain.follow.FollowRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.usecase.flick.response.AuthorInfo
import com.example.flick.usecase.flick.response.FlickDetailResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedGetUseCase(
    private val flickRepository: FlickRepository,
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
    private val likeRepository: com.example.flick.domain.like.LikeRepository // Fully qualified name to avoid conflict with LikeRepository in usecase module
) {
    @Transactional(readOnly = true)
    fun getFeed(authUserId: Long): List<FlickDetailResponse> {
        val followingIds = followRepository.findFollowingIdsByFollowerId(authUserId)

        // 自分自身の投稿もフィードに含める
        val userIdsToFetch = followingIds.toMutableList().apply { add(authUserId) }

        val flicks = flickRepository.findByUserIdsOrderByCreatedAtDesc(userIdsToFetch)

        return flicks.map { flick ->
            val author = userRepository.findById(flick.userId)
                ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

            val likeCount = likeRepository.countByFlickId(flick.id!!)
            val isLiked = likeRepository.isLiked(authUserId, flick.id!!)

            FlickDetailResponse(
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
}
