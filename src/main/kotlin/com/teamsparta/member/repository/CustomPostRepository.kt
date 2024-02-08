package com.teamsparta.member.repository

import com.teamsparta.member.domain.Post
import com.teamsparta.member.dto.PostSearchType
import com.teamsparta.member.dto.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPostRepository {

    fun search(keyword: String, searchType: PostSearchType, pageable: Pageable): Page<Post>

    fun findPostByPageableAndUserRole(pageable: Pageable, userRole: UserRole?): Page<Post>
}