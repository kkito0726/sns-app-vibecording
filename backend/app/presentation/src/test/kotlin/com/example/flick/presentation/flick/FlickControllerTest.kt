package com.example.flick.presentation.flick

import com.example.flick.presentation.TestSupport
import com.example.flick.presentation.flick.request.CommentCreationRequest
import com.example.flick.presentation.util.UserUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class FlickControllerTest : TestSupport() {

    @Autowired
    lateinit var userUtil: UserUtil

    val BASE_ENDPOINT = "/api/flicks"

    @Test
    fun `GET_idで指定したFlickが取得できる`() {
        val user = userUtil.createUser()
        val jwt = userUtil.login(user.email, "Test_pass_12345678")
        val flick = userUtil.createFlick(jwt)

        mockMvc.perform(
            MockMvcRequestBuilders.get("$BASE_ENDPOINT/${flick.id}")
                .header("Authorization", "Bearer $jwt")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(flick.id))
            .andExpect(jsonPath("$.textContent").value(flick.textContent))
            .andExpect(jsonPath("$.postType").value(flick.postType.name))
            .andExpect(jsonPath("$.author.userId").value(user.id))
            .andExpect(jsonPath("$.author.username").value(user.username))
            .andExpect(jsonPath("$.likeCount").value(0))
            .andExpect(jsonPath("$.isLiked").value(false))
    }

    @Test
    fun `DELETE_idで指定したFlickが削除できる`() {
        val user = userUtil.createUser()
        val jwt = userUtil.login(user.email, "Test_pass_12345678")
        val flick = userUtil.createFlick(jwt)

        mockMvc.perform(
            MockMvcRequestBuilders.delete("$BASE_ENDPOINT/${flick.id}")
                .header("Authorization", "Bearer $jwt")
        )
            .andExpect(status().isNoContent)

        // 削除されたFlickが取得できないことを確認
        mockMvc.perform(
            MockMvcRequestBuilders.get("$BASE_ENDPOINT/${flick.id}")
                .header("Authorization", "Bearer $jwt")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET_feedでフォロー中のユーザーのFlickと自分のFlickが取得できる`() {
        val user1 = userUtil.createAndLoginUser("user1", "user1@example.com")
        val flick1 = userUtil.createFlick(user1.token, "Flick by user1")

        val user2 = userUtil.createAndLoginUser("user2", "user2@example.com")
        val flick2 = userUtil.createFlick(user2.token, "Flick by user2")

        val user3 = userUtil.createAndLoginUser("user3", "user3@example.com")
        val flick3 = userUtil.createFlick(user3.token, "Flick by user3")

        // user1がuser2をフォロー
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users/${user2.user.id}/follow")
                .header("Authorization", "Bearer ${user1.token}")
        ).andExpect(status().isNoContent)

        // user1のフィードを取得
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("$BASE_ENDPOINT/feed")
                .header("Authorization", "Bearer ${user1.token}")
        )
            .andExpect(status().isOk)

        val responseBody = createResponseJsonArray(resultActions)
        // user1とuser2のFlickが含まれ、user3のFlickは含まれないことを確認
        val flickIdsInFeed = (0 until responseBody.length()).map {
            responseBody.getJSONObject(it).getLong("id")
        }
        assertTrue(flickIdsInFeed.contains(flick1.id))
        assertTrue(flickIdsInFeed.contains(flick2.id))
        assertFalse(flickIdsInFeed.contains(flick3.id))
    }

    @Test
    fun `POST_commentsでFlickにコメントが投稿できる`() {
        val user = userUtil.createAndLoginUser()
        val jwt = userUtil.login(user.user.email, "Test_pass_12345678")
        val flick = userUtil.createFlick(user.token)

        val commentText = "This is a test comment."
        val request = CommentCreationRequest(commentText)

        mockMvc.perform(
            MockMvcRequestBuilders.post("$BASE_ENDPOINT/${flick.id}/comments")
                .header("Authorization", "Bearer $jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.userId").value(user.user.id))
            .andExpect(jsonPath("$.flickId").value(flick.id))
            .andExpect(jsonPath("$.text").value(commentText))
            .andExpect(jsonPath("$.authorUsername").value(user.user.username))
    }

    @Test
    fun `GET_commentsでFlickのコメント一覧が取得できる`() {
        val user = userUtil.createUser()
        val jwt = userUtil.login(user.email, "Test_pass_12345678")
        val flick = userUtil.createFlick(jwt)

        // コメントを複数投稿
        val comment1 = userUtil.createComment(user.id, flick.id, "Comment 1")
        val comment2 = userUtil.createComment(user.id, flick.id, "Comment 2")

        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("$BASE_ENDPOINT/${flick.id}/comments")
                .header("Authorization", "Bearer $jwt")
        )
            .andExpect(status().isOk)

        val responseBody = createResponseJsonArray(resultActions)
        val commentTexts = (0 until responseBody.length()).map {
            responseBody.getJSONObject(it).getString("text")
        }
        assertEquals(2, commentTexts.size)
        assertTrue(commentTexts.contains("Comment 1"))
        assertTrue(commentTexts.contains("Comment 2"))
    }
}
