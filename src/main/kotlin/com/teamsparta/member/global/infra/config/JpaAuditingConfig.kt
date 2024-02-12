package com.teamsparta.member.global.infra.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

//@Configuration
@SpringBootApplication
@EnableJpaAuditing
class JpaAuditingConfig {
}
