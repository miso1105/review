package com.teamsparta.member.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamsparta.member.domain.Post
import com.teamsparta.member.domain.QMember.member
import com.teamsparta.member.domain.QPost
import com.teamsparta.member.dto.PostSearchType
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.global.infra.querydsl.QueryDslSupport
import com.teamsparta.member.global.infra.querydsl.byPaging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): QueryDslSupport(), CustomPostRepository {

    private val post = QPost.post

    override fun search(keyword: String, searchType: PostSearchType, pageable: Pageable): Page<Post> {
        return byPaging(pageable, post) {
            jpaQueryFactory.selectFrom(post)
                .where(
                    when (searchType) {
                        PostSearchType.TITLE -> post.title.like("%$keyword%")
                        PostSearchType.CONTENT -> post.content.like("%$keyword%")
                        PostSearchType.CREATED_BY -> post.createdBy.like("%$keyword%")
                        PostSearchType.NONE -> null
                    }
                )
        }
    }


    override fun findPostByPageableAndUserRole(pageable: Pageable, userRole: UserRole?): Page<Post> {
        val whereClause = BooleanBuilder()

        userRole?.let { whereClause.and(post.member.role.eq(userRole)) }

        val totalCount = queryFactory.select((post.count())).from(post).where(whereClause).fetchOne() ?: 0L

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

