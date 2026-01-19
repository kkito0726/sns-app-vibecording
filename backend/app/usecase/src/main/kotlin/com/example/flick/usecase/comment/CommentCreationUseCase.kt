package com.example.flick.usecase.comment

import com.example.flick.domain.error.ErrorCode

import com.example.flick.domain.comment.Comment
import com.example.flick.domain.comment.CommentRepository
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.usecase.comment.input.CommentCreationInput
import com.example.flick.usecase.comment.response.CommentResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentCreationUseCase(
    private val commentRepository: CommentRepository,
    private val flickRepository: FlickRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createComment(input: CommentCreationInput): CommentResponse {
        // コメント対象のFlickが存在するか確認
        flickRepository.findById(input.flickId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_FLICK)

        // コメント投稿ユーザーが存在するか確認 (理論上は認証済みなので存在するはず)
        val author = userRepository.findById(input.userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        val comment = Comment(
            userId = input.userId,
            flickId = input.flickId,
            text = input.text
        )

        val savedComment = commentRepository.save(comment)

        return CommentResponse(
            id = savedComment.id!!,
            userId = savedComment.userId,
            flickId = savedComment.flickId,
            text = savedComment.text,
            createdAt = savedComment.createdAt!!,
            authorUsername = author.username.value,
            authorProfileImageUrl = author.profileImageUrl
        )
    }
}
