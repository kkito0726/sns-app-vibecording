package com.example.flick.usecase.follow

import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.follow.FollowRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FollowUseCase(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
) {
    @Transactional
    fun followUser(authUser: UserDetails, followingUserId: Long) {
        val authUserId = userRepository.findByUsername(Username(authUser.username))?.id
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)
        val followingUser = userRepository.findById(followingUserId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        if (authUserId == followingUser.id) {
            throw IllegalArgumentException("Cannot follow yourself.")
        }

        if (followRepository.isFollowing(authUserId, followingUser.id!!)) {
            // Already following, do nothing or throw a specific exception
            return
        }

        followRepository.follow(authUserId, followingUser.id!!)
    }

    @Transactional
    fun unfollowUser(authUser: UserDetails, followingUserId: Long) {
        val authUserId = userRepository.findByUsername(Username(authUser.username))?.id
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)
        val followingUser = userRepository.findById(followingUserId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        if (authUserId == followingUser.id) {
            throw IllegalArgumentException("Cannot unfollow yourself.")
        }

        if (!followRepository.isFollowing(authUserId, followingUser.id!!)) {
            // Not following, do nothing or throw a specific exception
            return
        }

        followRepository.unfollow(authUserId, followingUser.id!!)
    }
}