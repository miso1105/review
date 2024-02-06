package com.teamsparta.member.repository

import com.teamsparta.member.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomMemberRepository {
    fun findMembersByPageableAndInputStr(pageable: Pageable): Page<Member>
}