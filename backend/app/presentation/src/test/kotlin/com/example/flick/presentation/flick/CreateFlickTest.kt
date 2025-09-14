package com.example.flick.presentation.flick

import com.example.flick.domain.flick.model.PostType
import com.example.flick.infra.minio.MinioService
import com.example.flick.presentation.TestSupport
import com.example.flick.presentation.util.UserUtil
import com.example.flick.usecase.flick.response.FlickResponse
import org.assertj.db.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class CreateFlickTest : TestSupport() {

    companion object {
        const val ENDPOINT = "/api/flicks"
        private val TABLE_NAMES = listOf("flicks")
    }

    @Autowired
    lateinit var userUtil: UserUtil

    @MockBean
    lateinit var minioService: MinioService

    @Test
    fun `テキストのみのFlickを投稿できる`() {
        val authenticatedUser = userUtil.createAndLoginUser()
        val token = authenticatedUser.token

        val textContent = "Hello, this is a text flick!"

        val changes = createChanges(TABLE_NAMES).setStartPointNow()
        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.multipart(ENDPOINT)
                .file("imageFile", ByteArray(0)) // Empty file for image
                .file("videoFile", ByteArray(0)) // Empty file for video
                .param("postType", PostType.TEXT.name)
                .param("textContent", textContent)
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isCreated)
        changes.setEndPointNow()

        val response = objectMapper.readValue(
            resultAction.andReturn().response.contentAsString,
            FlickResponse::class.java
        )
        assertNotNull(response)
        assertNotNull(response.id)
        assertNotNull(response.id)
        assertTrue(response.id > 0)
        assertNotNull(response.textContent)

        // DBアサート
        assertThat(changes)
            .ofCreationOnTable("flicks")
            .hasNumberOfChanges(1)
            .change()
            .isCreation()
            .rowAtEndPoint()
            .value("id")
            .isNotNull()
            .value("user_id")
            .isEqualTo(authenticatedUser.user.id)
            .value("text_content")
            .isEqualTo(textContent)
            .value("image_url")
            .isNull()
            .value("video_url")
            .isNull()
            .value("post_type")
            .isEqualTo(PostType.TEXT.name)
            .value("created_at")
            .isAfter(LocalDateTime.now().minusMinutes(1))
    }

    @Test
    fun `画像付きFlickを投稿できる`() {
        val authenticatedUser = userUtil.createAndLoginUser()
        val token = authenticatedUser.token

        val imageFile = MockMultipartFile(
            "imageFile",
            "test_image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "image content".toByteArray()
        )
        val textContent = "This is an image flick!"
        val dummyImageUrl = "http://localhost:9000/flick-bucket/flicks/test_image.jpg"

        `when`(minioService.uploadFile(imageFile)).thenReturn(dummyImageUrl)

        val changes = createChanges(TABLE_NAMES).setStartPointNow()
        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.multipart(ENDPOINT)
                .file(imageFile)
                .file("videoFile", ByteArray(0)) // Empty file for video
                .param("postType", PostType.IMAGE.name)
                .param("textContent", textContent)
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isCreated)
        changes.setEndPointNow()

        val response = objectMapper.readValue(
            resultAction.andReturn().response.contentAsString,
            FlickResponse::class.java
        )
        assertNotNull(response)
        assertNotNull(response.id)
        assertNotNull(response.id)
        assertTrue(response.id > 0)
        assertNotNull(response.textContent)
        assertNotNull(response.imageUrl)

        // DBアサート
        assertThat(changes)
            .ofCreationOnTable("flicks")
            .hasNumberOfChanges(1)
            .change()
            .isCreation()
            .rowAtEndPoint()
            .value("id")
            .isNotNull()
            .value("user_id")
            .isEqualTo(authenticatedUser.user.id)
            .value("text_content")
            .isEqualTo(textContent)
            .value("image_url")
            .isEqualTo(dummyImageUrl)
            .value("video_url")
            .isNull()
            .value("post_type")
            .isEqualTo(PostType.IMAGE.name)
            .value("created_at")
            .isAfter(LocalDateTime.now().minusMinutes(1))
    }

    @Test
    fun `動画付きFlickを投稿できる`() {
        val authenticatedUser = userUtil.createAndLoginUser()
        val token = authenticatedUser.token

        val videoFile = MockMultipartFile(
            "videoFile",
            "test_video.mp4",
            "video/mp4",
            "video content".toByteArray()
        )
        val textContent = "This is a video flick!"
        val dummyVideoUrl = "http://localhost:9000/flick-bucket/flicks/test_video.mp4"

        `when`(minioService.uploadFile(videoFile)).thenReturn(dummyVideoUrl)

        val changes = createChanges(TABLE_NAMES).setStartPointNow()
        val resultAction = mockMvc.perform(
            MockMvcRequestBuilders.multipart(ENDPOINT)
                .file("imageFile", ByteArray(0)) // Empty file for image
                .file(videoFile)
                .param("postType", PostType.VIDEO.name)
                .param("textContent", textContent)
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isCreated)
        changes.setEndPointNow()

        val response = objectMapper.readValue(
            resultAction.andReturn().response.contentAsString,
            FlickResponse::class.java)
        assertNotNull(response)
        assertNotNull(response.id)
        assertNotNull(response.id)
        assertTrue(response.id > 0)
        assertNotNull(response.textContent)
        assertNotNull(response.videoUrl)

        // DBアサート
        assertThat(changes)
            .ofCreationOnTable("flicks")
            .hasNumberOfChanges(1)
            .change()
            .isCreation()
            .rowAtEndPoint()
            .value("id")
            .isNotNull()
            .value("user_id")
            .isEqualTo(authenticatedUser.user.id)
            .value("text_content")
            .isEqualTo(textContent)
            .value("image_url")
            .isNull()
            .value("video_url")
            .isEqualTo(dummyVideoUrl)
            .value("post_type")
            .isEqualTo(PostType.VIDEO.name)
            .value("created_at")
            .isAfter(LocalDateTime.now().minusMinutes(1))
    }
}
