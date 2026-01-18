package com.example.flick.presentation.auth

import com.example.flick.presentation.auth.request.LoginUserRequest
import com.example.flick.usecase.auth.LoginUseCase
import com.example.flick.usecase.auth.input.LoginInput
import com.example.flick.usecase.auth.response.LoginResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "認証", description = "ユーザー認証関連のAPI")
@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val loginUseCase: LoginUseCase
) {
    @PostMapping("/login")
    @Operation(summary = "ユーザーログイン", description = "メールアドレスとパスワードを使用してログインし、JWTトークンを取得します。")
    fun login(@Valid @RequestBody request: LoginUserRequest): ResponseEntity<LoginResponse> {
        val useCaseRequest = LoginInput(request.email, request.password)
        return ResponseEntity.ok(loginUseCase.execute(useCaseRequest))
    }
}