package com.teamsparta.member.dto.res

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)

