package com.teamsparta.member.global.auth.jwt

import com.teamsparta.member.domain.Member
import com.teamsparta.member.dto.res.LoginResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.security.auth.Subject


@Component
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long,
    @Value("\${auth.jwt.refreshTokenExpirationHour}") private val refreshTokenExpirationHour: Long
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun generateJwt(member: Member): LoginResponse {
        val accessToken = generateToken(member)
        val refreshToken = generateRefreshToken(member)

        return LoginResponse(accessToken, refreshToken)
    }

    private fun generateToken(member: Member): String {
        val claims = mapOf(
            "nickName" to member.nickName,
            "email" to member.email,
            "role" to member.role
        )

        return Jwts.builder()
            .subject(member.id.toString())
            .issuer(issuer)
            .issuedAt(Date.from(Instant.now()))
            .claims(claims)
            .signWith(key)
            .expiration(Date.from(Instant.now().plus(Duration.ofHours(accessTokenExpirationHour))))
            .compact()
    }

    private fun generateRefreshToken(member: Member): String {
        val emptyClaims: Map<String, Any> = emptyMap()

        return Jwts.builder()
            .subject(member.id.toString())
            .issuer(issuer)
            .issuedAt(Date.from(Instant.now()))
            .claims(emptyClaims)
            .signWith(key)
            .expiration(Date.from(Instant.now().plus(Duration.ofHours(refreshTokenExpirationHour))))
            .compact()
    }


    fun validateToken(accessToken: String): Result<Jws<Claims>> {
        return kotlin.runCatching { Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken) } }
    }



