package com.example.flick.infra.user

import com.example.flick.domain.user.Email
import com.example.flick.domain.user.PasswordHash
import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
import com.example.flick.domain.user.Username
import com.example.flick.gen.jooq.tables.Users.USERS
import org.jooq.DSLContext
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
open class JooqUserRepository(
    private val dslContext: DSLContext,
    private val passwordEncoder: PasswordEncoder
) : UserRepository {

    override fun save(user: User): User {
        val now = LocalDateTime.now()
        return if (user.id == null) {
            // Insert new user
            val record = dslContext.newRecord(USERS).apply {
                set(USERS.USERNAME, user.username.value)
                set(USERS.EMAIL, user.email.value)
                set(USERS.PASSWORD_HASH, passwordEncoder.encode(user.passwordHash.value))
                set(USERS.CREATED_AT, now)
                set(USERS.UPDATED_AT, now)
            }
            record.insert()
            user.copy(
                id = record.get(USERS.ID),
                passwordHash = PasswordHash(record.get(USERS.PASSWORD_HASH)),
                createdAt = record.get(USERS.CREATED_AT),
                updatedAt = record.get(USERS.UPDATED_AT)
            )
        } else {
            // Update existing user
            val record = dslContext
                .selectFrom(USERS)
                .where(USERS.ID.eq(user.id))
                .fetchOne() ?: throw IllegalArgumentException("User not found with ID: ${user.id}")

            record.apply {
                set(USERS.USERNAME, user.username.value)
                set(USERS.EMAIL, user.email.value)
                // Only update passwordHash if it's a new hash (i.e., not already hashed)
                if (!passwordEncoder.matches(user.passwordHash.value, get(USERS.PASSWORD_HASH))) {
                    set(USERS.PASSWORD_HASH, passwordEncoder.encode(user.passwordHash.value))
                }
                set(USERS.UPDATED_AT, now)
            }
            record.update()
            user.copy(
                passwordHash = PasswordHash(record.get(USERS.PASSWORD_HASH)),
                updatedAt = record.get(USERS.UPDATED_AT)
            )
        }
    }

    override fun findByUsername(username: Username): User? {
        return dslContext.selectFrom(USERS)
            .where(USERS.USERNAME.eq(username.value))
            .fetchOne()
            ?.let {
                User(
                    id = it.get(USERS.ID),
                    username = Username(it.get(USERS.USERNAME)),
                    email = Email(it.get(USERS.EMAIL)),
                    passwordHash = PasswordHash(it.get(USERS.PASSWORD_HASH)),
                    createdAt = it.get(USERS.CREATED_AT),
                    updatedAt = it.get(USERS.UPDATED_AT)
                )
            }
    }

    override fun findByEmail(email: Email): User? {
        return dslContext.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email.value))
            .fetchOne()
            ?.let {
                User(
                    id = it.get(USERS.ID),
                    username = Username(it.get(USERS.USERNAME)),
                    email = Email(it.get(USERS.EMAIL)),
                    passwordHash = PasswordHash(it.get(USERS.PASSWORD_HASH)),
                    createdAt = it.get(USERS.CREATED_AT),
                    updatedAt = it.get(USERS.UPDATED_AT)
                )
            }
    }
}