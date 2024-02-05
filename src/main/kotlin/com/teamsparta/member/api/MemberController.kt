package com.teamsparta.member.api

import com.teamsparta.member.application.MemberService
import com.teamsparta.member.dto.req.LoginRequest
import com.teamsparta.member.dto.res.LoginResponse
import com.teamsparta.member.dto.req.SignUpRequest
import com.teamsparta.member.dto.res.EmailResponse
import com.teamsparta.member.dto.res.SignupResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignUpRequest): ResponseEntity<SignupResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signup(signupRequest))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.login(loginRequest))
    }

    @DeleteMapping("/logout")
    fun logout() : ResponseEntity<Unit> {
        memberService.logout()
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PostMapping("/sendmail")
    fun sendEmail(@RequestParam email: String): ResponseEntity<EmailResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.sendEmail(email))

    }
}