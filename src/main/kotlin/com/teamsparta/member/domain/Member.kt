package com.teamsparta.member.domain

import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.dto.req.SignUpRequest
import com.teamsparta.member.dto.res.MemberResponse
import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.Transient


@Entity
@Table(name = "member")
class Member(

    @Column(name = "email", unique = true)
    val email: String,


    @Column(name = "nickName")
    var nickName: String,


    @Column(name = "password")
    var password: String,


    @Column(name = "refreshtoken")
    var refreshToken: String? = null,


    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val role: UserRole,

    @Column(name = "created_at")
    val createdAt: LocalDateTime

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var id: Long? = null

    @Transient
    val rawPassword: String = ""


    fun encodePassword(encoder: PasswordEncoder): Member {
        this.password = encoder.encode(rawPassword)
        return this
    }

    fun saveRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun removeRefreshToken() {
        this.refreshToken = null
    }

    fun generateCode(): String {
        var randomStr = UUID.randomUUID().toString()
        randomStr = randomStr.replace("-","")
        randomStr = randomStr.substring(0, 11)

        return randomStr
    }

    fun from() = MemberResponse (
        id = this.id!!,
        email = this.email,
        nickName = this.nickName,
        createdAt = this.createdAt
    )

    companion object {
        fun of(request: SignUpRequest) = Member (
            email = request.email,
            nickName = request.nickName,
            password = request.password,
            role = request.role,
            createdAt = LocalDateTime.now()
        )
    }

}