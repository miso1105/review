package com.teamsparta.member.domain

import com.teamsparta.member.dto.req.PostRequest
import com.teamsparta.member.dto.res.PostResponse
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.security.access.AccessDeniedException

@Entity
@Table(name = "post")
class Post(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)  // delete 쿼리 한개만 생성해 삭제 가능
    @JoinColumn(name = "member_id")
    val member: Member

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    var id: Long? = null


    fun update(request: PostRequest, authenticatedId: Long) {
        if (verify(authenticatedId)) {
            title = request.title
            content = request.content
            return
        }

    }

    fun verify(authenticatedId: Long): Boolean {
        if (this.member.id == authenticatedId) {
            return true
        }
        throw AccessDeniedException("인증 실패")
    }


    fun from() = PostResponse(
        id = this.id!!,
        title = this.title,
        content = this.content
    )

    companion object {
        fun of(request: PostRequest, member: Member) = Post(
            title = request.title,
            content = request.content,
            member = member
        )
    }
}
