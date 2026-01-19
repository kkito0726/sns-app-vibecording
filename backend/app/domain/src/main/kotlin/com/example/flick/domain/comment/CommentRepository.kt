package com.example.flick.domain.comment

interface CommentRepository {
    fun save(comment: Comment): Comment
    fun findByFlickId(flickId: Long): List<Comment>
}
