package com.teamsparta.member.application

import com.teamsparta.member.domain.Post
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.dto.req.PostRequest
import com.teamsparta.member.dto.res.PostResponse
import com.teamsparta.member.global.auth.AuthenticationUtil.getUserEmail
import com.teamsparta.member.global.auth.AuthenticationUtil.getUserId
import com.teamsparta.member.global.exception.common.NoSuchEntityException
import com.teamsparta.member.repository.MemberRepository
import com.teamsparta.member.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,

) {
    fun getPaginatedPostList(pageable: Pageable, role: String?): Page<PostResponse> {
        val userRole = when(role) {
            "MEMBER" -> UserRole.MEMBER
            "ADMIN" -> UserRole.ADMIN
            null -> null
            else -> throw IllegalArgumentException()
        }
        return postRepository.findPostByPageableAndUserRole(pageable, userRole).map { it.from() }
    }

    fun getPostById(postId: Long): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw IllegalArgumentException()

        return post.from()
    }

    @Transactional
    fun createPost(request: PostRequest): PostResponse {
        val authenticatedEmail = getUserEmail()
        val member = memberRepository.findMemberByEmail(authenticatedEmail) ?: throw NoSuchEntityException()
        val post = Post.of(request, member)

        postRepository.save(post)

        return post.from()
    }

    @Transactional
    fun updatePost(postId: Long, request: PostRequest): PostResponse {
        val authenticatedId = getUserId()
        val post = postRepository.findByIdOrNull(postId) ?: throw NoSuchEntityException()

        post.update(request, authenticatedId)

        return post.from()
    }

    fun deletePost(postId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw NoSuchEntityException()
        postRepository.delete(post)

    }


}
