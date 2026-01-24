package com.example.flick.presentation.util

import com.example.flick.domain.error.NotFoundException
import com.example.flick.domain.flick.model.PostType
import com.example.flick.domain.user.Email
import com.example.flick.domain.user.Password
import com.example.flick.domain.user.UserRepository
import com.example.flick.presentation.auth.request.LoginUserRequest
import com.example.flick.presentation.user.UserTest
import com.example.flick.presentation.user.request.RegisterUserRequest
import com.example.flick.usecase.comment.input.CommentCreationInput
import com.example.flick.usecase.comment.response.CommentResponse
import com.example.flick.usecase.flick.input.FlickCreationInput
import com.example.flick.usecase.flick.response.FlickResponse
import com.example.flick.usecase.user.response.UserResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

data class AuthenticatedUser(val user: UserResponse, val token: String)

@Component
class UserUtil : UtilSupport() {

    @Autowired
    lateinit var userRepository: UserRepository
    fun createUser(
        username: String = "テスト 太郎",
        email: String = "test_email@test.gmail.com",
        password: String = "Test_pass_12345678",
    ): UserResponse {
        val request = RegisterUserRequest(
            username,
            email,
            password
        )
        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.post(UserTest.ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk)
        val responseBody = createResponseBodyJson(resultAction)
        return UserResponse(
            id = responseBody.getString("id").toLong(),
            username = responseBody.getString("username"),
            email = responseBody.getString("email")
        )
    }

    fun login(email: String, password: String): String {
        val request = LoginUserRequest(email, password)
        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk)
        val responseBody = createResponseBodyJson(resultAction)
        return responseBody.getString("token")
    }

    fun createAndLoginUser(
        username: String = "テスト 太郎",
        email: String = "test_email@test.gmail.com",
        password: String = "Test_pass_12345678",
    ): AuthenticatedUser {
        val user = createUser(username, email, password)

        val loginRequest = LoginUserRequest(
            user.email,
            password
        )

        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk)

        val responseBody = createResponseBodyJson(resultAction)
        val token = responseBody.getString("token")

        return AuthenticatedUser(user, token)
    }

    fun createFlick(token: String, textContent: String = "Test Flick"): FlickResponse {
        val multipartFile = MockMultipartFile("imageFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".toByteArray())
        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/flicks")
                .file(multipartFile)
                .param("postType", PostType.TEXT.name)
                .param("textContent", textContent)
                .header("Authorization", "Bearer $token")
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        val responseBody = createResponseBodyJson(resultAction)
        return FlickResponse(
            id = responseBody.getLong("id"),
            userId = responseBody.getLong("userId"),
            textContent = responseBody.getString("textContent"),
            imageUrl = if (responseBody.has("imageUrl")) responseBody.getString("imageUrl") else null,
            videoUrl = if (responseBody.has("videoUrl")) responseBody.getString("videoUrl") else null,
            postType = PostType.valueOf(responseBody.getString("postType")),
            createdAt = LocalDateTime.parse(responseBody.getString("createdAt"))
        )
    }

    fun createComment(userId: Long, flickId: Long, text: String): CommentResponse {
        val token = login("test_email@test.gmail.com", "Test_pass_12345678") // userIdに対応するユーザーでログイン
        val request = CommentCreationInput(userId, flickId, text)

        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/flicks/$flickId/comments")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        val responseBody = createResponseBodyJson(resultAction)
        return CommentResponse(
            id = responseBody.getLong("id"),
            userId = responseBody.getLong("userId"),
            flickId = responseBody.getLong("flickId"),
            text = responseBody.getString("text"),
            createdAt = LocalDateTime.parse(responseBody.getString("createdAt")),
            authorUsername = responseBody.getString("authorUsername"),
            authorProfileImageUrl = if (responseBody.has("authorProfileImageUrl")) responseBody.getString("authorProfileImageUrl") else null
        )
    }
}
