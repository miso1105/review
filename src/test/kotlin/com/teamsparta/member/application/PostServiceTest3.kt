package com.teamsparta.member.application

import com.teamsparta.member.dto.res.PostResponse
import com.teamsparta.member.repository.MemberRepository
import com.teamsparta.member.repository.PostRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

internal class PostServiceTest3: BehaviorSpec ({

    val memberRepository = mockk<MemberRepository>()
    val postRepository = mockk<PostRepository>()
    val postService = PostService(memberRepository, postRepository)

    val savedPostId = 1L
    val notSavedPostId = 10L
    val postResponse = PostResponse(
        id = savedPostId,
        title = "title",
        content = "content",
        createdAt = LocalDateTime.now(),
        createdBy = "test@gmail.com"
    )

    every { postRepository.findByIdOrNull(savedPostId)!!.from() } returns postResponse

    given("존재하는 Post 조회") {
        val targetId = savedPostId
        val targetPostResponse = PostResponse(
            id = savedPostId,
            title = "title",
            content = "content",
            createdAt = LocalDateTime.now(),
            createdBy = "test@gmail.com"
        )
        `when`("getPostById 메서드를 실행하면") {
            val result = postRepository.findByIdOrNull(targetId)!!.from()
            then("결과는 null 이 아니다") {
                result shouldNotBe null
                result.id shouldBe targetPostResponse.id
            }
        }
    }

    every { postRepository.findByIdOrNull(notSavedPostId)?.from() } returns null

    given("존재하지 않는 Post 조회") {
        val targetId = notSavedPostId
        `when`("getPostById 메서드를 실행하면") {
            val result = postRepository.findByIdOrNull(targetId)?.from()
            then("결과는 null 값이다") {
                result shouldBe null
            }
        }
    }

    Given("Post 가 존재하지 않을 때") {
        When("특정 Post 를 요청하면") {
            Then("IllegalArgumentException 발생해야한다") {
                val postId = 1L
                every { postRepository.findByIdOrNull(any()) } returns null

                shouldThrow<IllegalArgumentException> { postService.getPostById(postId) }
            }
        }
    }

})