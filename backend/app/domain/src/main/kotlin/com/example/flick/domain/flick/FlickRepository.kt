package com.example.flick.domain.flick

interface FlickRepository {
    fun save(flick: Flick): Flick
}
