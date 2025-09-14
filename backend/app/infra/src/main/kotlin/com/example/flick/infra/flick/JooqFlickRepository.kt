package com.example.flick.infra.flick

import com.example.flick.domain.flick.Flick
import com.example.flick.domain.flick.FlickRepository
import com.example.flick.gen.jooq.tables.Flicks.FLICKS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
open class JooqFlickRepository(
    private val dslContext: DSLContext,
) : FlickRepository {

    override fun save(flick: Flick): Flick {
        val now = LocalDateTime.now()
        return if (flick.id == null) {
            val record = dslContext.newRecord(FLICKS).apply {
                userId = flick.userId
                textContent = flick.textContent
                imageUrl = flick.imageUrl
                videoUrl = flick.videoUrl
                postType = flick.postType.name // Store enum name as string
                createdAt = now
            }
            record.insert()
            flick.copy(
                id = record.id,
                createdAt = record.createdAt
            )
        } else {
            // For now, we only support creation. Update logic can be added later if needed.
            throw UnsupportedOperationException("Updating Flick is not supported yet.")
        }
    }
}