package com.teamsparta.member.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.teamsparta.member.application.PostService
import com.teamsparta.member.domain.Member
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.dto.res.PostResponse
import com.teamsparta.member.global.auth.jwt.JwtPlugin
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class PostControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val jwtPlugin: JwtPlugin
): DescribeSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    val postService = mockk<PostService>()

    describe("GET/posts/{id}") {
        context("존재하는 ID를 요청할 때") {
            it("200 status code를 응답한다.")
                val postId = 1L

            every { postService.getPostById(any()) } returns PostResponse(
                id = postId,
                title = "test_title",
                content = "content"
            )

            val jwtToken = jwtPlugin.generateJwt(
                member = Member("test@gmail.com","test","1234","", UserRole.MEMBER)
            )
            val result = mockMvc.perform(
                get("/api/v1/posts/$postId")
                    .header("Authorization", "Bearer $jwtToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)   //없어도 됨

            ).andReturn()

            result.response.status shouldBe 200

            val responseDto = jacksonObjectMapper().readValue(
                result.response.contentAsString,
                PostResponse::class.java
            )

            responseDto.id shouldBe postId
        }
    }
})