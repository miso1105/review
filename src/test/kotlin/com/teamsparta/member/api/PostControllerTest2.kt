package com.teamsparta.member.api

import com.teamsparta.member.application.PostService
import com.teamsparta.member.dto.res.PostResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class PostControllerTest2: BehaviorSpec ({
    val savedPostId = 1L
    val postService = mockk<PostService>()
    val postController = PostController(postService)

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


})