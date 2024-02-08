package com.teamsparta.member.domain

import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.dto.req.SignUpRequest
import com.teamsparta.member.dto.res.SignupResponse
import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID
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
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var id: Long? = null

    @Transient
    val rawPassword: String = ""

//    @Transient
//    val emailCode: String = ""


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

    // 반환
    fun from() = SignupResponse (
        email = this.email,
        nickName = this.nickName
    )

    companion object {
        // toEntity
        fun of(request: SignUpRequest) = Member (
            email = request.email,
            nickName = request.nickName,
            password = request.password,
            role = request.role
        )
    }


//        fun ofMember(req: SignUpRequest) {
//
//        }
//
//        fun ofAdmin() {
//
//        }
//    }

}