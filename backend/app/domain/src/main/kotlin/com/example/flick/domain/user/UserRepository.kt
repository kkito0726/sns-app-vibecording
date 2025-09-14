package com.example.flick.domain.user

interface UserRepository {
    fun findByEmail(email: Email): User?
    fun findByUsername(username: Username): User?
    fun save(user: User): User
}
