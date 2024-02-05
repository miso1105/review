package com.teamsparta.member.application

import com.teamsparta.member.repository.MemberRepository
import com.teamsparta.member.repository.PostRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
@ExtendWith(MockKExtension::class)
class PostServiceTest: BehaviorSpec ({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    val memberRepository = mockk<MemberRepository>()
    val postRepository = mockk<PostRepository>()

    // 서비스에서 생성자 주입 썼던 순서대로 써야한다
    val postService = PostService(memberRepository, postRepository)

    Given("Post 목록이 존재하지 않을 때") {
        When("특정 Post를 요청하면") {
            Then("IllegalArgumentException 발생해야한다") {

                val postId = 1L
                every { postRepository.findByIdOrNull(any()) } returns null

                shouldThrow<IllegalArgumentException> {
                    postService.getPostById(postId)
                }
            }
        }
    }
})