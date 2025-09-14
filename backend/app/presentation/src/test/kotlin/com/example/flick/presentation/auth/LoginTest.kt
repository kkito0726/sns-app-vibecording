package com.example.flick.presentation.auth

import com.example.flick.presentation.TestSupport
import com.example.flick.presentation.auth.request.LoginUserRequest
import com.example.flick.presentation.util.UserUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class LoginTest : TestSupport() {
    @Autowired
    lateinit var userUtil: UserUtil

    val ENDPOINT = "/api/auth/login"

    @Test
    fun ログインできる() {
        val user = userUtil.createUser()

        val request = LoginUserRequest(
            user.email,
            "Test_pass_12345678"
        )

        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.post(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk)

        // レスポンスのアサート
        val responseBody = createResponseBodyJson(resultAction)
        Assertions.assertNotNull(responseBody.getString("token"))
    }
}