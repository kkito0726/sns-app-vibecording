package com.example.flick.presentation.user

import com.example.flick.presentation.user.request.RegisterUserRequest
import com.example.flick.usecase.user.UserRegistrationUseCase
import com.example.flick.usecase.user.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.flick.usecase.user.input.UserRegistrationInput
import com.example.flick.usecase.user.response.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRegistrationUseCase: UserRegistrationUseCase,
    private val userService: UserService
) {
    @PostMapping("/register")
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

    // ユーザープロフィール取得
    @GetMapping("/{username}")
    fun getUserProfile(
        @PathVariable username: String,
        @AuthenticationPrincipal currentUser: UserDetails? // 認証済みユーザーの情報を取得
    ): ResponseEntity<UserResponse> {
        val currentUserId = currentUser?.username?.let { userService.getUserProfile(it, null).id } // 認証済みユーザーのIDを取得
        val userProfile = userService.getUserProfile(username, currentUserId)
        return ResponseEntity(userProfile, HttpStatus.OK)
    }

    // ユーザーをフォロー
    @PostMapping("/{username}/follow")
    fun followUser(
        @PathVariable username: String,
        @AuthenticationPrincipal currentUser: UserDetails // 認証済みユーザーの情報を取得
    ): ResponseEntity<Void> {
        val followerId = userService.getUserProfile(currentUser.username, null).id // フォローするユーザーのIDを取得
        userService.followUser(followerId!!, username)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    // ユーザーのフォロー解除
    @DeleteMapping("/{username}/follow")
    fun unfollowUser(
        @PathVariable username: String,
        @AuthenticationPrincipal currentUser: UserDetails // 認証済みユーザーの情報を取得
    ): ResponseEntity<Void> {
        val followerId = userService.getUserProfile(currentUser.username, null).id // フォロー解除するユーザーのIDを取得
        userService.unfollowUser(followerId!!, username)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
