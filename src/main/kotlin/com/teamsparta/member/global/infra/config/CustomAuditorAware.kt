package com.teamsparta.member.global.infra.config

import com.teamsparta.member.global.auth.AuthenticationUtil.getUserEmail
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomAuditorAware: AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.ofNullable(getUserEmail())
    }
}