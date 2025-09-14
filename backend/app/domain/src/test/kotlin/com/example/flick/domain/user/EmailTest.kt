package com.example.flick.domain.user

import com.example.flick.domain.error.BusinessLogicException
import com.example.flick.domain.error.ErrorCode
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
        val exception = assertThrows(BusinessLogicException::class.java) {
            Email(" ")
        }
        assertEquals(ErrorCode.INVALID_INPUT.message, exception.errorCode.message)
    }

    @Test
    fun `アットマークを含まないメールアドレスでIllegalArgumentExceptionがスローされること`() {
        val exception = assertThrows(BusinessLogicException::class.java) {
            Email("invalid-email.com")
        }
        assertEquals(ErrorCode.INVALID_INPUT.message, exception.errorCode.message)
    }
}