package com.teamsparta.member.dto.res

import java.time.LocalDateTime


data class MemberResponse(
    val id: Long,
    val email: String,
    val nickName: String,
    val createdAt: LocalDateTime
)
