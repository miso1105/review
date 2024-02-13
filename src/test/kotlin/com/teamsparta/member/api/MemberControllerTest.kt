package com.teamsparta.member.api

import com.teamsparta.member.application.MemberService
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.dto.req.EmailRequest
import com.teamsparta.member.dto.req.LoginRequest
import com.teamsparta.member.dto.req.SignUpRequest
import com.teamsparta.member.dto.res.EmailResponse
import com.teamsparta.member.dto.res.LoginResponse
import com.teamsparta.member.dto.res.MemberResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

internal class MemberControllerTest: BehaviorSpec ({
    val memberService = mockk<MemberService>()
    val memberController = MemberController(memberService)

    val memberSignUpReq = SignUpRequest(email =" sample1@naver.com", nickName = "test", role = UserRole.MEMBER, password = "test1234")
    every { memberService.signup(memberSignUpReq) } returns MemberResponse(email =" sample1@naver.com", nickName = "test", id = 1L, createdAt = LocalDateTime.now())

    given("새로운 유저의 회원가입") {
        val targetMember = memberSignUpReq

        `when`("signup 메서드를 실행한다면") {
            val result = memberController.signup(targetMember)

            then("상태코드 201이 반환된다") {
                result.statusCode shouldBe HttpStatus.CREATED
                result.body?.email shouldBe targetMember.email
                result.body?.nickName shouldBe targetMember.nickName
            }
        }
    }


    val memberLoginReq = LoginRequest(email = "sample1@naver.com", "test1234")
    every { memberService.login(memberLoginReq) } returns LoginResponse(accessToken = "발급 받은 accessToken", refreshToken = "발급받은 refreshToken")

    given("회원가입 한 유저의 로그인") {
        val targetMember = memberLoginReq

        `when`("login 메서드를 실행한다면") {
            val result = memberController.login(targetMember)

            then("상태코드 200이 반환된다") {
                result.statusCode shouldBe HttpStatus.OK
            }
        }
    }

    every { memberService.logout() } returns Unit

    given("로그인 한 유저") {
        `when`("logout 메서드를 실행한다면") {
            val result = memberController.logout()

            then("상태코드 200이 반환된다") {
                result.statusCode shouldBe HttpStatus.OK
            }
        }
    }

    every { memberService.withDraw() } returns Unit

    given("로그아웃 한 유저") {
        `when`("withDraw 메서드를 실행한다면") {
            val result = memberController.withDraw()

            then("상태코드 200이 반환된다") {
                result.statusCode shouldBe HttpStatus.OK
            }
        }
    }

    val emailReq = EmailRequest(email = "sample1@naver.com")
    every { memberService.sendEmail(emailReq) } returns EmailResponse(message = "인증번호가 발송되었습니다")

    given("인증번호 서비스를 요청한 유저") {
        val targetEmail = emailReq

        `when`("sendEmail 메서드를 실행한다면") {
            val result = memberController.sendEmail(targetEmail)

            then("상태코드 200이 반환된다") {
                result.statusCode shouldBe HttpStatus.OK
            }
        }
    }

})
