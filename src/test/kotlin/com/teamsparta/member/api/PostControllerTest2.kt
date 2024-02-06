package com.teamsparta.member.api

import com.teamsparta.member.application.PostService
import com.teamsparta.member.dto.res.PostResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class PostControllerTest2: BehaviorSpec ({
    val savedPostId = 1L
    // 주입 뫀
    val postService = mockk<PostService>()
    // 테스트 할 레이어
    val postController = PostController(postService)

    val savedPost = PostResponse(savedPostId,"title", "content")
    every { postService.getPostById(savedPostId) } returns savedPost

    Given("a saved post Id") {
        val targetPostId = savedPostId

        When("execute getPost method") {
            val result = postController.getPost(targetPostId)


            Then("status code should be ok") {
                result.statusCode shouldBe HttpStatus.OK
                result.body?.id shouldBe savedPost.id       // ResponseEntity body 의 아이디가 저장된 post 의 id 여야 한다.
            }
        }
    }


})