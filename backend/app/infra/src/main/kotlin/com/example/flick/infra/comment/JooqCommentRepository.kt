package com.example.flick.infra.comment

import com.example.flick.domain.comment.Comment
import com.example.flick.domain.comment.CommentRepository
import com.example.flick.gen.jooq.tables.references.COMMENTS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JooqCommentRepository(
    private val dslContext: DSLContext
) : CommentRepository {
    override fun save(comment: Comment): Comment {
        val now = LocalDateTime.now()
        val record = dslContext.insertInto(COMMENTS)
            .set(COMMENTS.USER_ID, comment.userId)
            .set(COMMENTS.FLICK_ID, comment.flickId)
            .set(COMMENTS.TEXT, comment.text)
            .set(COMMENTS.CREATED_AT, now)
            .returningResult(COMMENTS.ID, COMMENTS.CREATED_AT)
            .fetchOne()!!

        return comment.copy(
            id = record.get(COMMENTS.ID)!!,
            createdAt = record.get(COMMENTS.CREATED_AT)!!
        )
    }

    override fun findByFlickId(flickId: Long): List<Comment> {
        return dslContext.selectFrom(COMMENTS)
            .where(COMMENTS.FLICK_ID.eq(flickId))
            .orderBy(COMMENTS.CREATED_AT.asc())
            .fetch()
            .map {
                Comment(
                    id = it.get(COMMENTS.ID)!!,
                    userId = it.get(COMMENTS.USER_ID)!!,
                    flickId = it.get(COMMENTS.FLICK_ID)!!,
                    text = it.get(COMMENTS.TEXT)!!,
                    createdAt = it.get(COMMENTS.CREATED_AT)!!
                )
            }
    }
}
