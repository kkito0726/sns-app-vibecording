package com.example.flick.presentation.user

import com.example.flick.presentation.TestSupport
import com.example.flick.presentation.user.request.RegisterUserRequest
import org.assertj.db.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

class UserTest : TestSupport() {
    companion object {
        const val ENDPOINT = "/api/users/register"
        private val TABLE_NAMES = listOf("users")
    }

    @Test
    fun ユーザー登録できる() {
        val request = RegisterUserRequest(
            "テスト 太郎",
            "test_email@test.gmail.com",
            "Test_pass_12345678"
        )
        val changes = createChanges(TABLE_NAMES).setStartPointNow()
        val resultAction = mockMvc.perform(
            post(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
        changes.setEndPointNow()

        // レスポンスのアサート
        val responseBody = createResponseBodyJson(resultAction)
        assertNotNull(responseBody.getString("id"))
        assertEquals(request.email, responseBody.getString("email"))
        assertEquals(request.username, responseBody.getString("username"))

        // DBアサート
        assertThat(changes)
            .ofModificationOnTable("users")
            .hasNumberOfChanges(0)
            .ofDeletionOnTable("users")
            .hasNumberOfChanges(0)
            .ofCreationOnTable("users")
            .hasNumberOfChanges(1)
            .change()
            .isCreation()
            .rowAtEndPoint()
            .value("id")
            .isNotNull()
            .value("username")
            .isEqualTo(request.username)
            .value("email")
            .isEqualTo(request.email)
            .value("password_hash")
            .isNotNull()
            .value("profile_image_url")
            .isNull()
            .value("bio")
            .isNull()
            .value("created_at")
            .isAfter(LocalDate.now().atStartOfDay())
            .value("updated_at")
            .isAfter(LocalDate.now().atStartOfDay())
    }
}