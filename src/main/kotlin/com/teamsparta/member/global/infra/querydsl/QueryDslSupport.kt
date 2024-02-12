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

// 직접 사용하는 것이 아니니까 추상 클래스로 만들어주고, 상속 받는 클래스들이 쿼리팩토리와 엔티티매니저를 사용할 수 있게
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

