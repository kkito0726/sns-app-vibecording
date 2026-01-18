package com.example.flick.presentation.flick

import com.example.flick.presentation.security.UserDetailsImpl
import com.example.flick.usecase.like.LikeUsecase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Flick(投稿)", description = "Flick(投稿)の作成や取得などを行うAPI")
@RestController
@RequestMapping("/api/flicks")
class LikeController(
    private val likeUsecase: LikeUsecase
) {

    @PostMapping("/{id}/like")
    @Operation(summary = "投稿へのいいね", description = "指定した投稿に「いいね」を追加します。")
    fun likeFlick(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable("id") flickId: Long
    ): ResponseEntity<Unit> {
        likeUsecase.likeFlick(userDetails.getUserId(), flickId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "投稿へのいいね解除", description = "指定した投稿の「いいね」を解除します。")
    fun unlikeFlick(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable("id") flickId: Long
    ): ResponseEntity<Unit> {
        likeUsecase.unlikeFlick(userDetails.getUserId(), flickId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
