package com.example.flick.usecase.like

import com.example.flick.domain.like.Like
import com.example.flick.domain.like.LikeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LikeUsecase(
    private val likeRepository: LikeRepository
) {

    fun likeFlick(userId: Long, flickId: Long) {
        val existingLike = likeRepository.findByUserIdAndFlickId(userId, flickId)
        if (existingLike == null) {
            likeRepository.save(Like(userId = userId, flickId = flickId))
        }
        // Already liked, do nothing
    }

    fun unlikeFlick(userId: Long, flickId: Long) {
        val existingLike = likeRepository.findByUserIdAndFlickId(userId, flickId)
        if (existingLike != null) {
            likeRepository.delete(userId, flickId)
        }
        // Not liked, do nothing
    }
}
