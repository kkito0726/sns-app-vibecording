package com.example.flick.usecase.user

import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.follow.FollowRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.usecase.user.response.UserResponse
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProfileUseCase(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {



    @Transactional(readOnly = true)
    fun getUserProfile(userId: Long, authUser: UserDetails): UserResponse {
        val authUserId = userRepository.findByUsername(Username(authUser.username))?.id
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)
        val user = userRepository.findById(userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        val followerCount = followRepository.getFollowerCount(user.id!!)
        val followingCount = followRepository.getFollowingCount(user.id!!)
        // ログインユーザーが検索対象のユーザーをフォーローしているか？
        val isFollowing = followRepository.isFollowing(authUserId, user.id!!)

        return UserResponse(
            id = user.id!!,
            username = user.username.value,
            email = user.email.value,
            profileImageUrl = user.profileImageUrl,
            bio = user.bio,
            followerCount = followerCount,
            followingCount = followingCount,
            isFollowing = isFollowing
        )
    }
}