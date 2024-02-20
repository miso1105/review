package com.teamsparta.member.application

import com.teamsparta.member.domain.Member
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.dto.req.PostRequest
import com.teamsparta.member.dto.res.PostResponse
import com.teamsparta.member.global.auth.AuthenticationUtil
import com.teamsparta.member.global.auth.UserPrincipal
import com.teamsparta.member.global.auth.jwt.JwtPlugin
import com.teamsparta.member.global.exception.common.NoSuchEntityException
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
//    val jwtPlugin = mockk<JwtPlugin>()
//    val loginResponse = jwtPlugin.generateJwt(Member(email = "sample1@naver.com", password = "aaaa", refreshToken = "test", nickName = "sample1", isDeleted = false, role = UserRole.MEMBER, createdAt = LocalDateTime.now()))
//    jwtPlugin.validateToken(accessToken = loginResponse.accessToken)
    val principal = mockk<UserPrincipal>()
    val authenticationUtil = mockk<AuthenticationUtil>()
    every { principal.email } returns authenticationUtil.getUserEmail()
    every { authenticationUtil.getUserEmail() } returns "test@gmail.com"

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
    val postRequest = PostRequest(title = "title", content = "content")

    every { postRepository.findByIdOrNull(savedPostId)!!.from() } returns postResponse

    given("존재하는 Post 조회") {
        val targetId = savedPostId
        val targetPostResponse = postResponse
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

    given("Post 가 존재하지 않을 때") {
        `when`("특정 Post 를 요청하면") {
            then("IllegalArgumentException 예외가 발생해야한다") {
                val postId = 1L
                every { postRepository.findByIdOrNull(any()) } returns null

                shouldThrow<IllegalArgumentException> { postService.getPostById(postId) }
            }
        }
    }


    //Caused by: java.lang.NullPointerException: Cannot invoke "org.springframework.security.core.Authentication.getPrincipal()"
    // because the return value of "org.springframework.security.core.context.SecurityContext.getAuthentication()" is null
    //Expected exception com.teamsparta.member.global.exception.common.NoSuchEntityException but a NullPointerException was thrown instead.

//    fun getAuthentication(): UserPrincipal = SecurityContextHolder.setContext().authentication.principal as UserPrincipal
//    fun getAuthentication(): UserPrincipal = UserPrincipal(id = 1L, email = "test@gmail.com", nickName = "test", role = setOf())
//    every { authenticationUtil.getUserEmail() } returns "test@gmail.com"
    every { memberRepository.findMemberByEmail(any()) } returns null
    given("Post 를 생성할 때") {
        `when`("유저의 이메일이 존재하지 않는다면") {
            memberRepository.findMemberByEmail("test@gmail.com")

            then("NoSuchEntityException 예외가 발생해야한다") {
                shouldThrow<NoSuchEntityException> { postService.createPost(request = postRequest) }

            }
        }
    }

})