package com.example.flick.domain.like

interface LikeRepository {
    fun save(like: Like)
    fun delete(userId: Long, flickId: Long)
    fun findByUserIdAndFlickId(userId: Long, flickId: Long): Like?
    fun countByFlickId(flickId: Long): Long
}
