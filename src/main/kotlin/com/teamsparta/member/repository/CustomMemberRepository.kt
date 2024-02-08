package com.teamsparta.member.repository

import com.teamsparta.member.domain.Member
import com.teamsparta.member.dto.MemberSearchType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomMemberRepository {

    fun search(keyword: String, searchType: MemberSearchType, pageable: Pageable): Page<Member>

    fun findMembersByPageableAndInputStr(pageable: Pageable): Page<Member>
}