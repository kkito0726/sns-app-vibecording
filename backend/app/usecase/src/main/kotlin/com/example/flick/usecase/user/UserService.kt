package com.example.flick.usecase.user

import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.follow.FollowRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.usecase.user.response.UserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    @Transactional
    fun followUser(followerId: Long, followingUsername: String) {
        val followingUser = userRepository.findByUsername(Username(followingUsername))
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        if (followerId == followingUser.id) {
            throw IllegalArgumentException("Cannot follow yourself.")
        }

        if (followRepository.isFollowing(followerId, followingUser.id!!)) {
            // Already following, do nothing or throw a specific exception
            return
        }

        followRepository.follow(followerId, followingUser.id!!)
    }

    @Transactional
    fun unfollowUser(followerId: Long, followingUsername: String) {
        val followingUser = userRepository.findByUsername(Username(followingUsername))
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        if (followerId == followingUser.id) {
            throw IllegalArgumentException("Cannot unfollow yourself.")
        }

        if (!followRepository.isFollowing(followerId, followingUser.id!!)) {
            // Not following, do nothing or throw a specific exception
            return
        }

        followRepository.unfollow(followerId, followingUser.id!!) 
    }

    @Transactional(readOnly = true)
    fun getUserProfile(username: String, currentUserId: Long?): UserResponse {
        val user = userRepository.findByUsername(Username(username))
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        val followerCount = followRepository.getFollowerCount(user.id!!)
        val followingCount = followRepository.getFollowingCount(user.id!!) 
        val isFollowing = currentUserId?.let { followRepository.isFollowing(it, user.id!!) } ?: false

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