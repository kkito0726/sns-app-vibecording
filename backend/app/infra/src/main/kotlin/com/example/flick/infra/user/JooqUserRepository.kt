package com.example.flick.infra.user

import com.example.flick.domain.user.User
import com.example.flick.domain.user.UserRepository
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
                set(USERS.USERNAME, user.username)
                set(USERS.EMAIL, user.email)
                set(USERS.PASSWORD_HASH, passwordEncoder.encode(user.passwordHash))
                set(USERS.CREATED_AT, now)
                set(USERS.UPDATED_AT, now)
            }
            record.insert()
            user.copy(
                id = record.get(USERS.ID),
                passwordHash = record.get(USERS.PASSWORD_HASH),
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
                set(USERS.USERNAME, user.username)
                set(USERS.EMAIL, user.email)
                // Only update passwordHash if it's a new hash (i.e., not already hashed)
                if (!passwordEncoder.matches(user.passwordHash, get(USERS.PASSWORD_HASH))) {
                    set(USERS.PASSWORD_HASH, passwordEncoder.encode(user.passwordHash))
                }
                set(USERS.UPDATED_AT, now)
            }
            record.update()
            user.copy(
                passwordHash = record.get(USERS.PASSWORD_HASH),
                updatedAt = record.get(USERS.UPDATED_AT)
            )
        }
    }

    override fun findByUsername(username: String): User? {
        return dslContext.selectFrom(USERS)
            .where(USERS.USERNAME.eq(username))
            .fetchOne()
            ?.let {
                User(
                    id = it.get(USERS.ID),
                    username = it.get(USERS.USERNAME),
                    email = it.get(USERS.EMAIL),
                    passwordHash = it.get(USERS.PASSWORD_HASH),
                    createdAt = it.get(USERS.CREATED_AT),
                    updatedAt = it.get(USERS.UPDATED_AT)
                )
            }
    }

    override fun findByEmail(email: String): User? {
        return dslContext.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .fetchOne()
            ?.let {
                User(
                    id = it.get(USERS.ID),
                    username = it.get(USERS.USERNAME),
                    email = it.get(USERS.EMAIL),
                    passwordHash = it.get(USERS.PASSWORD_HASH),
                    createdAt = it.get(USERS.CREATED_AT),
                    updatedAt = it.get(USERS.UPDATED_AT)
                )
            }
    }
}