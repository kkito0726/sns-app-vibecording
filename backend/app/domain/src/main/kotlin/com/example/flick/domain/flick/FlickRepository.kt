package com.example.flick.domain.flick

interface FlickRepository {
    fun save(flick: Flick): Flick
    fun findById(id: Long): Flick?
    fun deleteById(id: Long)
    fun findByUserIdsOrderByCreatedAtDesc(userIds: List<Long>): List<Flick>
}
