package com.teamsparta.member.application

import com.teamsparta.member.domain.Member
import com.teamsparta.member.domain.Post
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.repository.MemberRepository
import com.teamsparta.member.repository.PostRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
    val memberRepository = mockk<MemberRepository>()
    val postRepository = mockk<PostRepository>()
    // 서비스에서 생성자 주입 썼던 순서대로 써야한다
    val postService = PostService(memberRepository, postRepository)


    val savedPostId = 1L
    val notSavedPostId = 10L
    val savedPost = Post(
        title = "title",
        content = "content",
        member = Member("test@gmail.com","test", "test1234", null, UserRole.MEMBER)
    )
    every { postRepository.findByIdOrNull(savedPostId) } returns savedPost
    every { postRepository.findByIdOrNull(notSavedPostId) } returns null

    // 잘 반환하는지, 저장 잘 되는지 테스트
    Given("a saved post id") {
        val targetPostId = savedPostId

        When("execute findById") {
            val result = postRepository.findByIdOrNull(targetPostId)

            Then("result should not be null") {
                result shouldNotBe null
                result?.let {
                    it.id shouldBe savedPost.id
                    it.title shouldBe savedPost.title
                    it.content shouldBe savedPost.content
                    it.member shouldBe savedPost.member
                }
            }
        }
    }


    Given("a not saved post id") {
        val targetPostId = notSavedPostId

        When("execute findById") {
            val result = postRepository.findByIdOrNull(targetPostId)

            Then("result should be null") {
                result shouldBe null
            }
        }
    }

//    afterContainer {
//        clearAllMocks()
//    }


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
