package com.example.flick.infra.flick

import com.example.flick.domain.flick.Flick
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.domain.flick.model.PostType
import com.example.flick.gen.jooq.tables.references.FLICKS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class JooqFlickRepository(
    private val dslContext: DSLContext
) : FlickRepository {
    override fun save(flick: Flick): Flick {
        val record = dslContext.insertInto(FLICKS)
            .set(FLICKS.USER_ID, flick.userId)
            .set(FLICKS.TEXT_CONTENT, flick.textContent)
            .set(FLICKS.IMAGE_URL, flick.imageUrl)
            .set(FLICKS.VIDEO_URL, flick.videoUrl)
            .set(FLICKS.POST_TYPE, flick.postType.name)
            .returningResult(FLICKS.ID, FLICKS.CREATED_AT)
            .fetchOne()!!

        return flick.copy(
            id = record.get(FLICKS.ID)!!,
            createdAt = record.get(FLICKS.CREATED_AT)!!
        )
    }

    override fun findById(id: Long): Flick? {
        return dslContext.selectFrom(FLICKS)
            .where(FLICKS.ID.eq(id))
            .fetchOne()
            ?.let {
                Flick(
                    id = it.get(FLICKS.ID)!!,
                    userId = it.get(FLICKS.USER_ID)!!,
                    postType = PostType.valueOf(it.get(FLICKS.POST_TYPE)!!),
                    textContent = it.get(FLICKS.TEXT_CONTENT),
                    imageUrl = it.get(FLICKS.IMAGE_URL),
                    videoUrl = it.get(FLICKS.VIDEO_URL),
                    createdAt = it.get(FLICKS.CREATED_AT)!!
                )
            }
    }

    override fun deleteById(id: Long) {
        dslContext.deleteFrom(FLICKS)
            .where(FLICKS.ID.eq(id))
            .execute()
    }

    override fun findByUserIdsOrderByCreatedAtDesc(userIds: List<Long>): List<Flick> {
        return dslContext.selectFrom(FLICKS)
            .where(FLICKS.USER_ID.`in`(userIds))
            .orderBy(FLICKS.CREATED_AT.desc())
            .fetch()
            .map {
                Flick(
                    id = it.get(FLICKS.ID)!!,
                    userId = it.get(FLICKS.USER_ID)!!,
                    postType = PostType.valueOf(it.get(FLICKS.POST_TYPE)!!),
                    textContent = it.get(FLICKS.TEXT_CONTENT),
                    imageUrl = it.get(FLICKS.IMAGE_URL),
                    videoUrl = it.get(FLICKS.VIDEO_URL),
                    createdAt = it.get(FLICKS.CREATED_AT)!!
                )
            }
    }

}