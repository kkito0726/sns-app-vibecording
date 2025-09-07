package com.example.flick.usecase.user

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.Password
import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.domain.user.specification.UserRegistrationSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doThrow // Import doThrow
import org.mockito.Mockito.any // Import any

class UserRegistrationUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userRegistrationSpecification: UserRegistrationSpecification
    private lateinit var userRegistrationUseCase: UserRegistrationUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        userRegistrationSpecification = mock(UserRegistrationSpecification::class.java)
        userRegistrationUseCase = UserRegistrationUseCase(userRepository, userRegistrationSpecification)
    }

    @Test
    fun `新規ユーザーが正常に登録できること`() {
        val command = UserRegistrationCommand(
            username = "testuser",
            email = "test@example.com",
            password = "password123"
        )

        val newUser = User(
            username = Username(command.username),
            email = Email(command.email),
            password = Password(command.password)
        )
        val savedUser = newUser.copy(id = 1L)

        `when`(userRepository.save(newUser)).thenReturn(savedUser)

        val result = userRegistrationUseCase.registerUser(command)

        assertNotNull(result.id)
        assertEquals(command.username, result.username)
        assertEquals(command.email, result.email)
        verify(userRepository).save(newUser)
        verify(userRegistrationSpecification).checkUsernameUniqueness(Username(command.username))
        verify(userRegistrationSpecification).checkEmailUniqueness(Email(command.email))
    }

    @Test
    fun `既存のユーザー名で登録しようとするとIllegalArgumentExceptionがスローされること`() {
        val command = UserRegistrationCommand(
            username = "existingUser",
            email = "test@example.com",
            password = "password123"
        )

        doThrow(IllegalArgumentException("Username already exists"))
            .`when`(userRegistrationSpecification).checkUsernameUniqueness(Username(command.username))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            userRegistrationUseCase.registerUser(command)
        }
        assertEquals("Username already exists", exception.message)
        verify(userRegistrationSpecification).checkUsernameUniqueness(Username(command.username))
        verify(userRepository).save(any())
    }

    @Test
    fun `既存のメールアドレスで登録しようとするとIllegalArgumentExceptionがスローされること`() {
        val command = UserRegistrationCommand(
            username = "testuser",
            email = "existing@example.com",
            password = "password123"
        )

        doThrow(IllegalArgumentException("Email already exists"))
            .`when`(userRegistrationSpecification).checkEmailUniqueness(Email(command.email))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            userRegistrationUseCase.registerUser(command)
        }
        assertEquals("Email already exists", exception.message)
        verify(userRegistrationSpecification).checkEmailUniqueness(Email(command.email))
        verify(userRepository).save(any())
    }
}