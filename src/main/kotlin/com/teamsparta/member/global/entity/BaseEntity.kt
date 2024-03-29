package com.teamsparta.member.global.entity

import com.teamsparta.member.global.infra.config.CustomAuditorAware
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseEntity (

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: String = "system",

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: String = "system"
    )