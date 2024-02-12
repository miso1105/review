package com.teamsparta.member.repository

import com.teamsparta.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long>, CustomMemberRepository {

    fun existsMemberByEmail(email:String): Boolean

    fun findMemberByEmail(email: String): Member?



}