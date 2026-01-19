package com.example.flick.presentation.flick

import com.example.flick.domain.flick.model.PostType
import com.example.flick.presentation.flick.request.CommentCreationRequest
import com.example.flick.presentation.security.UserDetailsImpl
import com.example.flick.usecase.comment.CommentCreationUseCase
import com.example.flick.usecase.comment.CommentGetUseCase
import com.example.flick.usecase.comment.response.CommentResponse
import com.example.flick.usecase.flick.FeedGetUseCase
import com.example.flick.usecase.flick.FlickCreationUseCase
import com.example.flick.usecase.flick.FlickDeleteUseCase
import com.example.flick.usecase.flick.FlickGetUseCase
import com.example.flick.usecase.flick.input.FlickCreationInput
import com.example.flick.usecase.comment.input.CommentCreationInput
import com.example.flick.usecase.flick.response.FlickDetailResponse
import com.example.flick.usecase.flick.response.FlickResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Flick(投稿)", description = "Flick(投稿)の作成や取得などを行うAPI")
@RestController
@RequestMapping("/api/flicks")
class FlickController(
    private val flickCreationUseCase: FlickCreationUseCase,
    private val flickGetUseCase: FlickGetUseCase,
    private val flickDeleteUseCase: FlickDeleteUseCase,
    private val feedGetUseCase: FeedGetUseCase,
    private val commentCreationUseCase: CommentCreationUseCase,
    private val commentGetUseCase: CommentGetUseCase
) {
    @PostMapping
    @Operation(summary = "Flick(投稿)の作成", description = "新しいFlick(テキスト、画像、または動画)を投稿します。")
    fun createFlick(
        @RequestParam("postType") postType: PostType,
        @RequestParam(value = "textContent", required = false) textContent: String?,
        @RequestPart(value = "imageFile", required = false) imageFile: MultipartFile?,
        @RequestPart(value = "videoFile", required = false) videoFile: MultipartFile?
    ): ResponseEntity<FlickResponse> {
        val input = FlickCreationInput(
            textContent = textContent,
            imageFile = imageFile,
            videoFile = videoFile,
            postType = postType
        )
        return ResponseEntity(
            flickCreationUseCase.createFlick(input),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/{id}")
    @Operation(summary = "Flick(投稿)の取得", description = "指定したIDのFlick(投稿)を一件取得します。")
    fun getFlick(
        @PathVariable("id") flickId: Long,
        @AuthenticationPrincipal authUser: UserDetailsImpl?
    ): ResponseEntity<FlickDetailResponse> {
        val flick = flickGetUseCase.getFlick(flickId, authUser?.getUserId())
        return ResponseEntity.ok(flick)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Flick(投稿)の削除", description = "指定したIDのFlick(投稿)を削除します。")
    fun deleteFlick(
        @PathVariable("id") flickId: Long,
        @AuthenticationPrincipal authUser: UserDetailsImpl
    ): ResponseEntity<Void> {
        flickDeleteUseCase.deleteFlick(flickId, authUser.getUserId())
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/feed")
    @Operation(summary = "Flickフィードの取得", description = "フォローしているユーザーのFlickと自分自身のFlickを時系列順に取得します。")
    fun getFeed(
        @AuthenticationPrincipal authUser: UserDetailsImpl
    ): ResponseEntity<List<FlickDetailResponse>> {
        val feed = feedGetUseCase.getFeed(authUser.getUserId())
        return ResponseEntity.ok(feed)
    }

    @PostMapping("/{flickId}/comments")
    @Operation(summary = "Flickにコメントを投稿", description = "指定したFlickにコメントを投稿します。")
    fun createComment(
        @PathVariable("flickId") flickId: Long,
        @AuthenticationPrincipal authUser: UserDetailsImpl,
        @Valid @RequestBody request: CommentCreationRequest
    ): ResponseEntity<CommentResponse> {
        val input = CommentCreationInput(
            userId = authUser.getUserId(),
            flickId = flickId,
            text = request.text
        )
        val comment = commentCreationUseCase.createComment(input)
        return ResponseEntity(comment, HttpStatus.CREATED)
    }

    @GetMapping("/{flickId}/comments")
    @Operation(summary = "Flickのコメント一覧を取得", description = "指定したFlickのコメント一覧を取得します。")
    fun getComments(
        @PathVariable("flickId") flickId: Long
    ): ResponseEntity<List<CommentResponse>> {
        val comments = commentGetUseCase.getComments(flickId)
        return ResponseEntity.ok(comments)
    }
}