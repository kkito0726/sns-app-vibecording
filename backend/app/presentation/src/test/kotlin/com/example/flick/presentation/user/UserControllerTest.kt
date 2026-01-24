package com.example.flick.presentation.user

import com.example.flick.presentation.TestSupport
import com.example.flick.presentation.user.request.UpdateUserProfileRequest
import com.example.flick.presentation.util.UserUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class UserControllerTest : TestSupport() {

    @Autowired
    lateinit var userUtil: UserUtil

    val BASE_ENDPOINT = "/api/users"

    @Test
    fun `GET_meで認証済みユーザーのプロフィールが取得できる`() {
        // ユーザー作成と認証
        val user = userUtil.createUser()
        val jwt = userUtil.login(user.email, "Test_pass_12345678")

        mockMvc.perform(
            MockMvcRequestBuilders.get("$BASE_ENDPOINT/me")
                .header("Authorization", "Bearer $jwt")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id))
            .andExpect(jsonPath("$.username").value(user.username))
            .andExpect(jsonPath("$.email").value(user.email))
            .andExpect(jsonPath("$.profileImageUrl").isEmpty)
            .andExpect(jsonPath("$.bio").isEmpty)
            .andExpect(jsonPath("$.followerCount").value(0))
            .andExpect(jsonPath("$.followingCount").value(0))
            .andExpect(jsonPath("$.isFollowing").value(false))
    }

    @Test
    fun `PUT_meで認証済みユーザーのプロフィールが更新できる`() {
        // ユーザー作成と認証
        val user = userUtil.createUser()
        val jwt = userUtil.login(user.email, "Test_pass_12345678")

        val newUsername = "updated_username"
        val newBio = "Updated bio content."
        val newProfileImageContent = "profile_image_bytes".toByteArray()
        val newProfileImageFile = MockMultipartFile(
            "profileImage",
            "profile.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            newProfileImageContent
        )

        val requestDto = UpdateUserProfileRequest(
            username = newUsername,
            bio = newBio
        )

        val requestJson = objectMapper.writeValueAsString(requestDto)
        val requestPart = MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, requestJson.toByteArray())

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("$BASE_ENDPOINT/me")
                .file(newProfileImageFile)
                .file(requestPart)
                .header("Authorization", "Bearer $jwt")
                .with { it.method = "PUT"; it } // PUTメソッドとして認識させる
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id))
            .andExpect(jsonPath("$.username").value(newUsername))
            .andExpect(jsonPath("$.email").value(user.email))
            .andExpect(jsonPath("$.profileImageUrl").isNotEmpty) // MinIOに保存されるのでURLが入る想定
            .andExpect(jsonPath("$.bio").value(newBio))
    }

    @Test
    fun `PUT_meでプロフィール画像なしで更新できる`() {
        val user = userUtil.createUser()
        val jwt = userUtil.login(user.email, "Test_pass_12345678")

        val newUsername = "updated_username_no_image"
        val newBio = "Updated bio content without image."

        val requestDto = UpdateUserProfileRequest(
            username = newUsername,
            bio = newBio
        )

        val requestJson = objectMapper.writeValueAsString(requestDto)
        val requestPart = MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, requestJson.toByteArray())

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("$BASE_ENDPOINT/me")
                .file(requestPart)
                .header("Authorization", "Bearer $jwt")
                .with { it.method = "PUT"; it }
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id))
            .andExpect(jsonPath("$.username").value(newUsername))
            .andExpect(jsonPath("$.email").value(user.email))
            .andExpect(jsonPath("$.profileImageUrl").isEmpty)
            .andExpect(jsonPath("$.bio").value(newBio))
    }
}
