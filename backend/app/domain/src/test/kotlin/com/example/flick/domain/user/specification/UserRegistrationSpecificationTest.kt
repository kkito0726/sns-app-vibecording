package com.example.flick.domain.user.specification

import com.example.flick.domain.error.ConflictException
import com.example.flick.domain.user.Email
import com.example.flick.domain.user.Password
import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class UserRegistrationSpecificationTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userRegistrationSpecification: UserRegistrationSpecification

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        userRegistrationSpecification = UserRegistrationSpecification(userRepository)
    }

    @Test
    fun `ユーザー名が一意であれば例外をスローしないこと`() {
        val username = Username("uniqueUser")
        `when`(userRepository.findByUsername(username)).thenReturn(null)

        assertDoesNotThrow {
            userRegistrationSpecification.checkUsernameUniqueness(username)
        }
    }

    @Test
    fun `ユーザー名が既に存在すればConflictExceptionをスローすること`() {
        val username = Username("existingUser")
        `when`(userRepository.findByUsername(username)).thenReturn(
            User(
                username = username,
                email = Email("test@example.com"),
                password = Password("hash")
            )
        )

        val exception = assertThrows(ConflictException::class.java) {
            userRegistrationSpecification.checkUsernameUniqueness(username)
        }
        assertEquals("このユーザー名は既に使用されています。", exception.errorCode.message)
    }

    @Test
    fun `メールアドレスが一意であれば例外をスローしないこと`() {
        val email = Email("unique@example.com")
        `when`(userRepository.findByEmail(email)).thenReturn(null)

        assertDoesNotThrow {
            userRegistrationSpecification.checkEmailUniqueness(email)
        }
    }

    @Test
    fun `メールアドレスが既に存在すればConflictExceptionをスローすること`() {
        val email = Email("existing@example.com")
        `when`(userRepository.findByEmail(email)).thenReturn(
            User(
                username = Username("test"),
                email = email,
                password = Password("hash")
            )
        )

        val exception = assertThrows(ConflictException::class.java) {
            userRegistrationSpecification.checkEmailUniqueness(email)
        }
        assertEquals("このメールアドレスは既に使用されています。", exception.errorCode.message)
    }
}