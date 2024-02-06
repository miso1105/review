package com.teamsparta.member.repository

import com.querydsl.core.BooleanBuilder
import com.teamsparta.member.domain.Member
import com.teamsparta.member.domain.QMember
import com.teamsparta.member.global.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl: QueryDslSupport(), CustomMemberRepository {

    private val member = QMember.member

    override fun findMembersByPageableAndInputStr(pageable: Pageable): Page<Member> {

        val whereClause = BooleanBuilder()

        val totalCount = queryFactory.select(member.count()).from(member).where(whereClause).fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(member)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, member))
            .fetch()


        return PageImpl(contents, pageable, totalCount)
    }
}