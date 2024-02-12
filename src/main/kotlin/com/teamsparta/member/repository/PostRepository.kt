package com.teamsparta.member.repository

import com.teamsparta.member.domain.Post
import com.teamsparta.member.dto.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>, CustomPostRepository {
}

