package com.teamsparta.member.global.infra.querydsl

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Pageable

abstract class QueryDslSupport {

    @PersistenceContext
    protected lateinit var entityManager: EntityManager

    protected val queryFactory: JPAQueryFactory
        get() {
            return JPAQueryFactory(entityManager)
        }


    fun getOrderSpecifier(pageable: Pageable, path: EntityPathBase<*>): Array<OrderSpecifier<*>> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map {
                order -> OrderSpecifier(
            if (order.isAscending) Order.ASC else Order.DESC,
            pathBuilder.get(order.property) as Expression<Comparable<*>>
        )
        }.toTypedArray()
    }
}
