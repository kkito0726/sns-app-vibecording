package com.example.flick.usecase.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class TokenProvider(
    @Value("\${jwt.secret:your-secret-key-that-is-long-enough-to-be-secure}")
    private val secret: String,
    @Value("\${jwt.expiration:3600000}")
    private val expiration: Long
) {

    fun createToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        val claims: Claims = Jwts.parser()
            .setSigningKey(secret.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secret.toByteArray()).build().parseClaimsJws(token)
            return true
        } catch (ex: Exception) {
            // Log the exception
        }
        return false
    }
}
