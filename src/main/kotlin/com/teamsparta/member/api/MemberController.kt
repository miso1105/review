package com.teamsparta.member.api

import com.teamsparta.member.application.MemberService
import com.teamsparta.member.dto.MemberSearchType
import com.teamsparta.member.dto.req.EmailRequest
import com.teamsparta.member.dto.req.LoginRequest
import com.teamsparta.member.dto.res.LoginResponse
import com.teamsparta.member.dto.req.SignUpRequest
import com.teamsparta.member.dto.res.EmailResponse
import com.teamsparta.member.dto.res.MemberResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService
) {
    @GetMapping("/search")
    fun getMemberListByKeyword(@RequestParam(value = "keyword", required = true) keyword: String, searchType: MemberSearchType, pageable: Pageable): ResponseEntity<Page<MemberResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.searchByMemberInfo(keyword, searchType, pageable))
    }

    @GetMapping
    fun getPaginatedMemberList(@PageableDefault (size = 5, sort = ["id"]) pageable: Pageable): ResponseEntity<Page<MemberResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getPaginatedMemberList(pageable))
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignUpRequest): ResponseEntity<MemberResponse> {
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

    @PutMapping("/withdraw")
    fun withDraw() : ResponseEntity<Unit> {
        return memberService.withDraw().let { ResponseEntity.status(HttpStatus.OK).build() }

    }

    @PostMapping("/sendmail")
    fun sendEmail(@RequestBody emailRequest: EmailRequest): ResponseEntity<EmailResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.sendEmail(emailRequest))
    }

}