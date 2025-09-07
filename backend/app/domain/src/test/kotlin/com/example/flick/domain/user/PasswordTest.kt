package com.example.flick.domain.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PasswordTest {

    @Test
    fun `有効なパスワードハッシュでPasswordHashが作成できること`() {
        val passwordHash = Password("hashedpassword123")
        assertEquals("hashedpassword123", passwordHash.value)
    }

    @Test
    fun `空白のパスワードハッシュでIllegalArgumentExceptionがスローされること`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Password(" ")
        }
        assertEquals("Password hash cannot be blank", exception.message)
    }
}