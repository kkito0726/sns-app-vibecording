package com.example.flick.usecase.flick

import com.example.flick.domain.error.ErrorCode
import com.example.flick.domain.error.ForbiddenException
import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.flick.FlickRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FlickDeleteUseCase(
    private val flickRepository: FlickRepository
) {
    @Transactional
    fun deleteFlick(flickId: Long, authUserId: Long) {
        val flick = flickRepository.findById(flickId)
            ?: throw NotFoundException(ErrorCode.NOT_FOUND_FLICK)

        if (flick.userId != authUserId) {
            throw ForbiddenException(ErrorCode.NOT_AUTH_DELETE_FLICK)
        }

        flickRepository.deleteById(flickId)
    }
}
