package com.example.flick.usecase.user.response

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val followerCount: Long = 0,
    val followingCount: Long = 0,
    val isFollowing: Boolean = false
)