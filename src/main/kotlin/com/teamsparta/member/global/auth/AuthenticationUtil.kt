package com.teamsparta.member.global.auth

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
object AuthenticationUtil {
    private fun getAuthentication(): UserPrincipal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal

    fun getUserId() = getAuthentication().id

    fun getUserEmail() = getAuthentication().email
}