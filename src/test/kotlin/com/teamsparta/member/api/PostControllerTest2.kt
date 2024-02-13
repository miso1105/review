package com.teamsparta.member.api

import com.teamsparta.member.application.PostService
import com.teamsparta.member.dto.req.PostRequest
import com.teamsparta.member.dto.res.PostResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

internal class PostControllerTest2: BehaviorSpec ({
    val savedPostId = 1L
    val postService = mockk<PostService>()
    val postController = PostController(postService)
    val postReq = PostRequest(title = "title", content = "content")
    val savedPost = PostResponse(id = savedPostId, title ="title", content = "content", createdAt = LocalDateTime.now(), createdBy = "test")

    every { postService.getPostById(savedPostId) } returns savedPost

    Given("a saved post Id") {
        val targetPostId = savedPostId

        When("execute getPost method") {
            val result = postController.getPost(targetPostId)


            Then("status code should be ok") {
                result.statusCode shouldBe HttpStatus.OK
                result.body?.id shouldBe savedPost.id
            }
        }
    }

    every { postService.createPost(postReq) } returns savedPost

    given("게시글 생성 요청") {
        val targetPost = postReq

        `when`("createPost 메서드를 실행한다면") {
            val result = postController.createPost(targetPost)

            then("상태코드 201이 반환된다") {
                result.statusCode shouldBe HttpStatus.CREATED
                result.body?.title shouldBe savedPost.title
                result.body?.content shouldBe savedPost.content
            }
        }
    }

    every { postService.updatePost(savedPostId, postReq) } returns savedPost

    given("게시글 수정 요청") {
        val targetPostId = savedPostId
        val targetPost = postReq

        `when`("updatePost 메서드를 실행한다면") {
            val result = postController.updatePost(targetPostId, targetPost)

            then("상태코드 200이 반환된다") {
                result.statusCode shouldBe HttpStatus.OK
                result.body?.id shouldBe targetPostId
            }
        }
    }

    every { postService.deletePost(savedPostId) } returns Unit

    given("게시글 삭제 요청") {
        val targetPostId = savedPostId

        `when`("deletePost 메서드를 실행한다면") {
            val result = postController.deletePost(targetPostId)

            then("상태코드 204가 반환된다") {
                result.statusCode shouldBe HttpStatus.NO_CONTENT
            }
        }
    }
})
