package com.example.flick.presentation.user

import com.example.flick.presentation.security.UserDetailsImpl
import com.example.flick.presentation.user.request.RegisterUserRequest
import com.example.flick.presentation.user.request.UpdateUserProfileRequest
import com.example.flick.usecase.follow.FollowUseCase
import com.example.flick.usecase.user.UserProfileUpdateUseCase
import com.example.flick.usecase.user.UserProfileUseCase
import com.example.flick.usecase.user.UserRegistrationUseCase
import com.example.flick.usecase.user.input.UserProfileUpdateInput
import com.example.flick.usecase.user.input.UserRegistrationInput
import com.example.flick.usecase.user.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "ユーザー", description = "ユーザーの登録、プロフィール取得、フォローなどを行うAPI")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRegistrationUseCase: UserRegistrationUseCase,
    private val userProfileUseCase: UserProfileUseCase,
    private val followUseCase: FollowUseCase,
    private val userProfileUpdateUseCase: UserProfileUpdateUseCase
) {
    @PostMapping("/register")
    @Operation(summary = "ユーザー新規登録", description = "新しいユーザーを登録します。")
    fun registerUser(@Valid @RequestBody request: RegisterUserRequest): ResponseEntity<UserResponse> {
        val input = UserRegistrationInput(
            username = request.username,
            email = request.email,
            password = request.password
        )
        return ResponseEntity(
            userRegistrationUseCase.registerUser(input),
            HttpStatus.OK
        )
    }

    @GetMapping("/me")
    @Operation(summary = "自分のプロフィール取得", description = "現在認証中のユーザーのプロフィール情報を取得します。")
    fun getMyProfile(
        @AuthenticationPrincipal authUser: UserDetails
    ): ResponseEntity<UserResponse> {
        val userId = (authUser as UserDetailsImpl).getUserId()
        val userProfile = userProfileUseCase.getUserProfile(userId, authUser)
        return ResponseEntity(userProfile, HttpStatus.OK)
    }

    @PutMapping("/me", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "自分のプロフィール更新", description = "現在認証中のユーザーのプロフィール情報（ユーザー名、自己紹介、プロフィール画像）を更新します。")
    fun updateMyProfile(
        @AuthenticationPrincipal authUser: UserDetailsImpl,
        @Valid @RequestPart("request") request: UpdateUserProfileRequest,
        @RequestPart(value = "profileImage", required = false) profileImage: MultipartFile?
    ): ResponseEntity<UserResponse> {
        val input = UserProfileUpdateInput(
            userId = authUser.getUserId(),
            username = request.username,
            bio = request.bio,
            profileImage = profileImage
        )
        val updatedUser = userProfileUpdateUseCase.updateUserProfile(input)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    // ユーザープロフィール取得
    @GetMapping("/{userId}")
    @Operation(summary = "ユーザープロフィール取得", description = "指定したユーザーIDのプロフィール情報を取得します。")
    fun getUserProfile(
        @PathVariable userId: Long,
        @AuthenticationPrincipal authUser: UserDetails
    ): ResponseEntity<UserResponse> {
        val userProfile = userProfileUseCase.getUserProfile(userId, authUser)
        return ResponseEntity(userProfile, HttpStatus.OK)
    }

    // ユーザーをフォロー
    @PostMapping("/{userId}/follow")
    @Operation(summary = "ユーザーのフォロー", description = "指定したユーザーIDのユーザーをフォローします。")
    fun followUser(
        @PathVariable userId: Long,
        @AuthenticationPrincipal authUser: UserDetails
    ): ResponseEntity<Void> {
        followUseCase.followUser(authUser, userId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    // ユーザーのフォロー解除
    @DeleteMapping("/{userId}/follow")
    @Operation(summary = "ユーザーのフォロー解除", description = "指定したユーザーIDのユーザーのフォローを解除します。")
    fun unfollowUser(
        @PathVariable userId: Long,
        @AuthenticationPrincipal authUser: UserDetails
    ): ResponseEntity<Void> {
        followUseCase.unfollowUser(authUser, userId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}

