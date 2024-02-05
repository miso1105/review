package com.teamsparta.member.repository

import com.teamsparta.member.domain.Post
import com.teamsparta.member.dto.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPostRepository {
    fun findPostByPageableAndUserRole(pageable: Pageable, userRole: UserRole?): Page<Post>
}