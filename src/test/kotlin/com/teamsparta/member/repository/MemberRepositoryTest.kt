package com.teamsparta.member.repository

import com.teamsparta.member.domain.Member
import com.teamsparta.member.dto.MemberSearchType
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.global.infra.querydsl.QueryDslConfig
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslConfig::class])
@ActiveProfiles("test")
class MemberRepositoryTest @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val entityManager: TestEntityManager
) {

    @Test
    fun sampleTest() {
        memberRepository.save(Member("test@gmail.com","test","1234",null, false, UserRole.ADMIN))
        entityManager.flush()  // 쓰기 지연 저장소에 쌓이지 않고 지금 실행하기 위해서 쓰기 지연 저장소를 비우는 플러쉬 직접 호출.
    }


    @Test
    fun `SearchType 이 None 일 경우 전체 데이터 조회되는지 확인`() {
        // GIVEN
        memberRepository.saveAllAndFlush(DEFAULT_MEMBER_LIST)

        // WHEN
        val result1 = memberRepository.search("", MemberSearchType.NONE,  Pageable.ofSize(10))
        val result2 = memberRepository.search("", MemberSearchType.NONE,  Pageable.ofSize(5))
        val result3 = memberRepository.search("", MemberSearchType.NONE,  Pageable.ofSize(20))

        // THEN - content == MemberSearchType.None 인 유저들
        result1.content.size shouldBe 10
        result2.content.size shouldBe 5
        result3.content.size shouldBe 10

    }

    @Test
    fun `SearchType 이 None 이 아닌 경우 keyword 에 의해 검색되는지 결과 확인`() {
        // GIVEN
        memberRepository.saveAllAndFlush(DEFAULT_MEMBER_LIST)

        // WHEN
        val result1 = memberRepository.search("gmail", MemberSearchType.EMAIL, Pageable.ofSize(10))
        val result2 = memberRepository.search("naver", MemberSearchType.EMAIL, Pageable.ofSize(10))
        val result3 = memberRepository.search("sample", MemberSearchType.NICKNAME, Pageable.ofSize(10))

        // THEN
        result1.content.size shouldBe 4
        result2.content.size shouldBe 4
        result3.content.size shouldBe 10
    }

    @Test
    fun `keyword 에 의한 검색 조회 결과가 0건일 경우 결과 확인`() {
        // GIVEN
        memberRepository.saveAllAndFlush(DEFAULT_MEMBER_LIST)

        // WHEN
        val result1 = memberRepository.search("yahoo", MemberSearchType.EMAIL, Pageable.ofSize(10))
        val result2 = memberRepository.search("bbbb", MemberSearchType.NICKNAME, Pageable.ofSize(10))

        // THEN
        result1.content.size shouldBe 0
        result2.content.size shouldBe 0
    }

    @Test
    fun `조회된 결과가 10개, PageSize 6일 때 0Page 결과 확인`() {
        // GIVEN
        memberRepository.saveAllAndFlush(DEFAULT_MEMBER_LIST)

        // WHEN
        val result = memberRepository.search("sample", MemberSearchType.NICKNAME, Pageable.ofSize(6))

        // THEN
        result.number shouldBe 0
        result.content.size shouldBe  6
        result.totalPages shouldBe 2
        result.isFirst shouldBe true
        result.isLast shouldBe false
        result.totalElements shouldBe 10
    }

    @Test
    fun `조회된 결과가 10개, PageSize 6일 때 1Page 결과 확인`() {
        memberRepository.saveAllAndFlush(DEFAULT_MEMBER_LIST)

        val result = memberRepository.search("sample", MemberSearchType.EMAIL, PageRequest.of(1,6))

        result.number shouldBe 1
        result.content.size shouldBe 4
        result.totalPages shouldBe 2
        result.isFirst shouldBe false
        result.isLast shouldBe true
        result.totalElements shouldBe 10
    }

    companion object {
        private val DEFAULT_MEMBER_LIST = listOf(
            Member(
                email = "sample1@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample1",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample2@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample2",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample3@daum.net",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample3",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample4@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample4",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample5@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample5",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample6@daum.net",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample6",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample7@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample7",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample8@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample8",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample9@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample9",
                isDeleted = false,
                role = UserRole.MEMBER
            ),
            Member(
                email = "sample10@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample10",
                isDeleted = false,
                role = UserRole.MEMBER
            )
        )
    }

}

