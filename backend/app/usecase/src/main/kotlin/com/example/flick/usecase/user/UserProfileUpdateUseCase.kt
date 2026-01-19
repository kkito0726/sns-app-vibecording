package com.example.flick.usecase.user

import com.example.flick.domain.common.FileStorageService
import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.follow.FollowRepository
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.usecase.user.input.UserProfileUpdateInput
import com.example.flick.usecase.user.response.UserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProfileUpdateUseCase(
    private val userRepository: UserRepository,
    private val fileStorageService: FileStorageService,
    private val followRepository: FollowRepository
) {

    @Transactional
    fun updateUserProfile(input: UserProfileUpdateInput): UserResponse {
        val user = userRepository.findById(input.userId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_USER)

        val newUsername = input.username?.let { Username(it) }
        val newBio = input.bio

        var newProfileImageUrl: String? = user.profileImageUrl
        if (input.profileImage != null && !input.profileImage.isEmpty) {
            newProfileImageUrl = fileStorageService.uploadFile(input.profileImage)
        }

        val userToUpdate = user.copy(
            username = newUsername ?: user.username,
            bio = newBio ?: user.bio,
            profileImageUrl = newProfileImageUrl
        )

        val updatedUser = userRepository.save(userToUpdate)

        // 更新後のユーザー情報を取得して返す
        val followerCount = followRepository.getFollowerCount(updatedUser.id!!)
        val followingCount = followRepository.getFollowingCount(updatedUser.id!!)

        return UserResponse(
            id = updatedUser.id!!,
            username = updatedUser.username.value,
            email = updatedUser.email.value,
            profileImageUrl = updatedUser.profileImageUrl,
            bio = updatedUser.bio,
            followerCount = followerCount,
            followingCount = followingCount,
            isFollowing = false // 自身のプロフィールなので常にfalse
        )
    }
}
