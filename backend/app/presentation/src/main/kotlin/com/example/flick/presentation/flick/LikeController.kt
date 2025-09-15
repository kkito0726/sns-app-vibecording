package com.example.flick.presentation.flick

import com.example.flick.presentation.security.UserDetailsImpl
import com.example.flick.usecase.like.LikeUsecase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/flicks")
class LikeController(
    private val likeUsecase: LikeUsecase
) {

    @PostMapping("/{id}/like")
    fun likeFlick(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable("id") flickId: Long
    ): ResponseEntity<Unit> {
        likeUsecase.likeFlick(userDetails.getUserId(), flickId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{id}/like")
    fun unlikeFlick(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable("id") flickId: Long
    ): ResponseEntity<Unit> {
        likeUsecase.unlikeFlick(userDetails.getUserId(), flickId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
