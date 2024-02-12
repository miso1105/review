package com.teamsparta.member.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamsparta.member.domain.Member
import com.teamsparta.member.domain.QMember
import com.teamsparta.member.dto.MemberSearchType
import com.teamsparta.member.global.infra.querydsl.QueryDslSupport
import com.teamsparta.member.global.infra.querydsl.byPaging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): QueryDslSupport(), CustomMemberRepository {

    private val member = QMember.member

    // 주입 방법
    override fun search(keyword: String, searchType: MemberSearchType, pageable: Pageable): Page<Member> {
        return byPaging(pageable, member) {
            jpaQueryFactory.selectFrom(member)
                .where(
                    when (searchType) {
                        MemberSearchType.EMAIL -> member.email.like("%$keyword%")
                        MemberSearchType.NICKNAME -> member.nickName.like("%$keyword%")
                        MemberSearchType.NONE -> null
                    }
                )
        }
    }

    // 추상 클래스 상속 방법
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