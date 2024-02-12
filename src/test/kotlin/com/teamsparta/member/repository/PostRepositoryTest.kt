package com.teamsparta.member.repository

import com.teamsparta.member.domain.Member
import com.teamsparta.member.domain.Post
import com.teamsparta.member.dto.PostSearchType
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.global.infra.querydsl.QueryDslConfig
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslConfig::class])
@ActiveProfiles("test")
class PostRepositoryTest @Autowired constructor(
    private val postRepository: PostRepository,
) {

    @Test
    fun `SearchType 이 NONE 일 경우 전체 데이터 조회되는지 확인`() {
        // GIVEN
        postRepository.saveAllAndFlush(DEFAULT_POST_LIST)

        // WHEN
        val result1 = postRepository.search("", PostSearchType.NONE, Pageable.ofSize(10))
        val result2 = postRepository.search("", PostSearchType.NONE, Pageable.ofSize(5))
        val result3 = postRepository.search("", PostSearchType.NONE, Pageable.ofSize(20))

        // THEN
        result1.content.size shouldBe 10
        result2.content.size shouldBe 5
        result3.content.size shouldBe 10
    }

    @Test
    fun `SearchType 이 NONE 이 아닌 경우 keyword 에 의해 검색되는지 결과 확인`() {
        // GIVEN
        postRepository.saveAllAndFlush(DEFAULT_POST_LIST)

        // WHEN
        val result1 = postRepository.search("sample", PostSearchType.TITLE, Pageable.ofSize(10))
        val result2 = postRepository.search("aaaa", PostSearchType.CONTENT, Pageable.ofSize(10))
        val result3 = postRepository.search("naver.com", PostSearchType.CREATED_BY, Pageable.ofSize(10))

        // THEN
        result1.content.size shouldBe 10
        result2.content.size shouldBe 3
        result3.content.size shouldBe 4
    }

    @Test
    fun `keyword 에 의해 조회된 결과가 0건일 경우 결과 확인`() {
        // GIVEN
        postRepository.saveAllAndFlush(DEFAULT_POST_LIST)

        // WHEN
        val result1 = postRepository.search("sampleeee", PostSearchType.TITLE, Pageable.ofSize(10))
        val result2 = postRepository.search("zzzz", PostSearchType.CONTENT, Pageable.ofSize(10))
        val result3 = postRepository.search("yahoo.com", PostSearchType.CREATED_BY, Pageable.ofSize(10))

        // THEN
        result1.content.size shouldBe 0
        result2.content.size shouldBe 0
        result3.content.size shouldBe 0
    }

    @Test
    fun `조회된 결과가 10개, PageSize 3 일 때 0Page 결과 확인`() {
        // GIVEN
        postRepository.saveAllAndFlush(DEFAULT_POST_LIST)

        // WHEN
        val result = postRepository.search("sample", PostSearchType.TITLE, PageRequest.of(0, 3))

        // THEN
        result.content.size shouldBe 3
        result.number shouldBe 0
        result.totalPages shouldBe 4
        result.totalElements shouldBe 10
        result.isFirst shouldBe true
    }

    @Test
    fun `조회된 결과가 10개, PageSize 3 일 때 3Page 결과 확인`() {
        // GIVEN
        postRepository.saveAllAndFlush(DEFAULT_POST_LIST)

        // WHEN
        val result = postRepository.search("sample", PostSearchType.CREATED_BY, PageRequest.of(3, 3))

        // THEN
        result.content.size shouldBe 1
        result.totalPages shouldBe 4
        result.totalElements shouldBe 10
        result.number shouldBe 3
        result.isLast shouldBe true
    }

    companion object {
        private val DEFAULT_POST_LIST = listOf(
            Post(title = "sample1",content = "aaaa", createdBy = "sample1@naver.com", Member(
                email = "sample1@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample1",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample2",content = "aaaa", createdBy ="sample2@gmail.com", Member(
                email = "sample2@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample2",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample3",content = "aaaa", createdBy ="sample3@daum.net", Member(
                email = "sample3@daum.net",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample3",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample4",content = "bbbb", createdBy ="sample4@gmail.com", Member(
                email = "sample4@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample4",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample5",content = "bbbb", createdBy ="sample5@naver.com", Member(
                email = "sample5@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample5",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample6", content = "bbbb", createdBy ="sample6@daum.net", Member(
                email = "sample6@daum.net",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample6",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample7",content = "cccc", createdBy ="sample7@naver.com", Member(
                email = "sample7@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample7",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample8",content = "cccc", createdBy ="sample8@gmail.com", Member(
                email = "sample8@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample8",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post(title = "sample9",content = "cccc", createdBy ="sample9@naver.com", Member(
                email = "sample9@naver.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample9",
                isDeleted = false,
                role = UserRole.MEMBER
            )),
            Post( title = "sample10",content = "cccc", createdBy ="sample10@gmail.com", Member(
                email = "sample10@gmail.com",
                password = "aaaa",
                refreshToken = null,
                nickName = "sample10",
                isDeleted = false,
                role = UserRole.MEMBER
            ))
        )
    }
}
