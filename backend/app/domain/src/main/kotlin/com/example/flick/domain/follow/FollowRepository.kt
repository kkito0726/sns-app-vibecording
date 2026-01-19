package com.example.flick.domain.follow

interface FollowRepository {
    fun follow(followerId: Long, followingId: Long)
    fun unfollow(followerId: Long, followingId: Long)
    fun isFollowing(followerId: Long, followingId: Long): Boolean
    fun getFollowerCount(userId: Long): Long
    fun getFollowingCount(userId: Long): Long
    fun findFollowingIdsByFollowerId(followerId: Long): List<Long>
}