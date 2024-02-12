package com.teamsparta.member.global.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val id: Long,
    val email: String,
    val nickName: String,
    val authorities: Collection<GrantedAuthority>
) {
    constructor(id: Long, email: String, nickName: String, role: Set<String>): this (
        id,
        email,
        nickName,
        role.map { SimpleGrantedAuthority("ROLE_$it")}
    )
}
