package com.example.flick.domain.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class EmailTest {

    @Test
    fun `有効なメールアドレスでEmailが作成できること`() {
        val email = Email("test@example.com")
        assertEquals("test@example.com", email.value)
    }

    @Test
    fun `空白のメールアドレスでIllegalArgumentExceptionがスローされること`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Email(" ")
        }
        assertEquals("Email cannot be blank", exception.message)
    }

    @Test
    fun `アットマークを含まないメールアドレスでIllegalArgumentExceptionがスローされること`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Email("invalid-email.com")
        }
        assertEquals("Invalid email format", exception.message)
    }
}