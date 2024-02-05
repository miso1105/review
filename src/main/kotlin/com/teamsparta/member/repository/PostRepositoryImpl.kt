package com.teamsparta.member.repository

import com.querydsl.core.BooleanBuilder
import com.teamsparta.member.domain.Post
import com.teamsparta.member.domain.QMember.member
import com.teamsparta.member.domain.QPost
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.global.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl: QueryDslSupport(), CustomPostRepository {         // 인터페이스 기반으로 빈을 찾아서 이 구현체를 인식하는 것

    private val post = QPost.post

    // userRole 이 널일 수 있으니까 동적쿼리로 구현해보기
    override fun findPostByPageableAndUserRole(pageable: Pageable, userRole: UserRole?): Page<Post> {
        val whereClause = BooleanBuilder()

        userRole?.let { whereClause.and(post.member.role.eq(userRole)) }

        val totalCount = queryFactory.select((post.count())).from(post).where(whereClause).fetchOne() ?: 0L

//        val query = queryFactory.selectFrom(post)
//            .where(whereClause)
//            .offset(pageable.offset)
//            .limit(pageable.pageSize.toLong())
//
//        if (pageable.sort.isSorted) {
//            if (pageable.sort.first()?.property == "id") {
//                query.orderBy((post.id.asc()))
//            } else if (pageable.sort.first()?.property == "title") {
//                query.orderBy(post.title.asc())
//            } else {
//                query.orderBy(post.id.asc())
//            }
//        }

        val contents = queryFactory.selectFrom(post)
            .where(whereClause)
            .leftJoin(post.member, member)
            .fetchJoin()
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, post))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}

