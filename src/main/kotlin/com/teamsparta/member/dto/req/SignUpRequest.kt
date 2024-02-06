package com.teamsparta.member.dto.req

import com.teamsparta.member.dto.UserRole


data class SignUpRequest(
    val email: String,
    val nickName: String,
    val role: UserRole,
    var password: String,
)
