package com.teamsparta.member.dto.req

import com.teamsparta.member.domain.Member
import com.teamsparta.member.dto.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.security.crypto.password.PasswordEncoder


data class SignUpRequest(
    val email: String,
    val nickName: String,
    val role: UserRole,
    var password: String,
)
