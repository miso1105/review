package com.teamsparta.member.api

import com.teamsparta.member.application.PostService
import com.teamsparta.member.dto.PostSearchType
import com.teamsparta.member.dto.UserRole
import com.teamsparta.member.dto.req.PostRequest
import com.teamsparta.member.dto.res.PostResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService
) {
    @GetMapping("/search")
    fun getPostListByKeyword(@RequestParam(value = "keyword") keyword: String, searchType: PostSearchType, pageable: Pageable): ResponseEntity<Page<PostResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(postService.search(keyword, searchType, pageable))
    }

    @GetMapping
    fun getPaginatedPostList(@PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
                             @RequestParam(value = "role", required = false) role: String?): ResponseEntity<Page<PostResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPaginatedPostList(pageable, role))
    }

    @GetMapping("/{postId}")
    fun getPost(@RequestParam postId: Long): ResponseEntity<PostResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(postId))
    }

    @PostMapping
    fun createPost(@RequestBody postRequest: PostRequest): ResponseEntity<PostResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest))
    }

    @PatchMapping("/{postId}")
    fun updatePost(@PathVariable postId: Long, @RequestBody postRequest: PostRequest): ResponseEntity<PostResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postId, postRequest))
    }

    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Long): ResponseEntity<Unit> {
        postService.deletePost(postId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
