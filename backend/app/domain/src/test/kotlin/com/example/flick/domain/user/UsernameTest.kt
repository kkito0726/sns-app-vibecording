package com.example.flick.domain.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class UsernameTest {

    @Test
    fun `有効な値でユーザー名が作成できること`() {
        val username = Username("testuser")
        assertEquals("testuser", username.value)
    }

    @Test
    fun `空白のユーザー名でIllegalArgumentExceptionがスローされること`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Username(" ")
        }
        assertEquals("Username cannot be blank", exception.message)
    }

    @Test
    fun `短すぎるユーザー名でIllegalArgumentExceptionがスローされること`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Username("ab")
        }
        assertEquals("Username must be between 3 and 50 characters", exception.message)
    }

    @Test
    fun `長すぎるユーザー名でIllegalArgumentExceptionがスローされること`() {
        val longUsername = "a".repeat(51)
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Username(longUsername)
        }
        assertEquals("Username must be between 3 and 50 characters", exception.message)
    }
}