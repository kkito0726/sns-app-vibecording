package com.example.flick.presentation.util

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.Password
import com.example.flick.presentation.user.UserTest
import com.example.flick.presentation.user.request.RegisterUserRequest
import com.example.flick.usecase.user.response.UserResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@Component
class UserUtil : UtilSupport() {
    fun createUser(
        username: String = "テスト 太郎",
        email: String = "test_email@test.gmail.com",
        password: String = "Test_pass_12345678",
    ): UserResponse {
        val request = RegisterUserRequest(
            "テスト 太郎",
            "test_email@test.gmail.com",
            "Test_pass_12345678"
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
}