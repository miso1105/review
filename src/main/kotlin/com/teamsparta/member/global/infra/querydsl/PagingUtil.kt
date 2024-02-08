package com.teamsparta.member.global.infra.querydsl

import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

fun <T> byPaging(pageable: Pageable, path: EntityPathBase<T>, baseQueryFunc: () -> JPAQuery<T>): Page<T> {
    val baseQuery = baseQueryFunc()
    val totalCount = baseQuery
        .select(path.count())
        .fetchOne() ?: 0L
    val result = baseQuery
        .select(path)
        .offset(pageable.offset)
        .limit(pageable.pageSize.toLong())
        .fetch()

    return PageImpl(result, pageable, totalCount)
}