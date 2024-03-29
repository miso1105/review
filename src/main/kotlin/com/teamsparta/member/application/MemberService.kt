package com.teamsparta.member.application

import com.teamsparta.member.domain.Member
import com.teamsparta.member.dto.MemberSearchType
import com.teamsparta.member.dto.req.EmailRequest
import com.teamsparta.member.dto.req.LoginRequest
import com.teamsparta.member.dto.req.SignUpRequest
import com.teamsparta.member.dto.res.EmailResponse
import com.teamsparta.member.dto.res.LoginResponse
import com.teamsparta.member.dto.res.MemberResponse
import com.teamsparta.member.global.auth.AuthenticationUtil.getUserEmail
import com.teamsparta.member.global.auth.jwt.JwtPlugin
import com.teamsparta.member.global.exception.common.DuplicateEmailException
import com.teamsparta.member.global.exception.common.NoSuchEntityException
import com.teamsparta.member.repository.MemberRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtPlugin: JwtPlugin,
    private val encoder: BCryptPasswordEncoder,
    private val javaMailSender: JavaMailSender
) {
    fun searchByMemberInfo(keyword: String, searchType: MemberSearchType , pageable: Pageable): Page<MemberResponse> {
        return memberRepository.search(keyword, searchType, pageable).map { it.from() }
    }

    fun getPaginatedMemberList(pageable: Pageable): Page<MemberResponse> {
        return memberRepository.findMembersByPageableAndInputStr(pageable).map { it.from() }
    }

    @Transactional
    fun signup(request: SignUpRequest): MemberResponse {

        check (!memberRepository.existsMemberByEmail(request.email)) { throw DuplicateEmailException() }

        val member = Member.of(request).encodePassword(encoder).let { memberRepository.save(it) }


        return member.from()
    }

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val member = memberRepository.findMemberByEmail(request.email) ?: throw NoSuchEntityException()
        val jwt = jwtPlugin.generateJwt(member)

        member.isDeleted = false
        member.saveRefreshToken(jwt.refreshToken)

        return jwt
    }

    @Transactional
    fun logout() {
        val authenticatedEmail = getUserEmail()
        val member = memberRepository.findMemberByEmail(authenticatedEmail) ?: throw NoSuchEntityException()

        member.removeRefreshToken()

    }

    @Transactional
    fun withDraw() {
        val member = memberRepository.findMemberByEmail(getUserEmail()) ?: throw NoSuchEntityException()
        member.isDeleted = true
        member.refreshToken = null
        memberRepository.save(member)
    }

    fun sendEmail(request: EmailRequest): EmailResponse {
        val member = memberRepository.findMemberByEmail(request.email) ?: throw NoSuchEntityException()
        val randomCode = member.generateCode()
        val mail = javaMailSender.createMimeMessage()
        val mailHelper = MimeMessageHelper(mail, "UTF-8")

        mailHelper.setFrom("misopak06@gmail.com")
        mailHelper.setTo(request.email)
        mailHelper.setSubject("할일 카드 인증번호!")
        mailHelper.setText(randomCode)
        javaMailSender.send(mail)

        return EmailResponse(message = "인증번호가 발송되었습니다")
    }


}

