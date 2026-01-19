package com.example.flick.usecase.comment

import com.example.flick.domain.comment.CommentRepository
import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.usecase.comment.response.CommentResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentGetUseCase(
    private val commentRepository: CommentRepository,
    private val flickRepository: FlickRepository,
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getComments(flickId: Long): List<CommentResponse> {
        // 対象のFlickが存在するか確認
        flickRepository.findById(flickId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_FLICK)

        val comments = commentRepository.findByFlickId(flickId)

        return comments.map { comment ->
            val author = userRepository.findById(comment.userId)
                ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

            CommentResponse(
                id = comment.id!!,
                userId = comment.userId,
                flickId = comment.flickId,
                text = comment.text,
                createdAt = comment.createdAt!!,
                authorUsername = author.username.value,
                authorProfileImageUrl = author.profileImageUrl
            )
        }
    }
}
