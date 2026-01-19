package com.example.flick.infra.like

import com.example.flick.domain.like.Like
import com.example.flick.domain.like.LikeRepository
import com.example.flick.gen.jooq.tables.references.LIKES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
open class JooqLikeRepository(
    private val dslContext: DSLContext,
) : LikeRepository {

    override fun save(like: Like) {
        val record = dslContext.newRecord(LIKES).apply {
            userId = like.userId
            flickId = like.flickId
            createdAt = LocalDateTime.now()
        }
        record.insert()
    }

    override fun delete(userId: Long, flickId: Long) {
        dslContext.deleteFrom(LIKES)
            .where(LIKES.USER_ID.eq(userId).and(LIKES.FLICK_ID.eq(flickId)))
            .execute()
    }

    override fun findByUserIdAndFlickId(userId: Long, flickId: Long): Like? {
        return dslContext.selectFrom(LIKES)
            .where(LIKES.USER_ID.eq(userId).and(LIKES.FLICK_ID.eq(flickId)))
            .fetchOne()
            ?.into(Like::class.java)
    }

    override fun countByFlickId(flickId: Long): Int {
        return dslContext.selectCount()
            .from(LIKES)
            .where(LIKES.FLICK_ID.eq(flickId))
            .fetchOne(0, Int::class.java)!!
    }

    override fun isLiked(userId: Long, flickId: Long): Boolean {
        return dslContext.fetchExists(
            dslContext.selectFrom(LIKES)
                .where(LIKES.USER_ID.eq(userId))
                .and(LIKES.FLICK_ID.eq(flickId))
        )
    }
}
