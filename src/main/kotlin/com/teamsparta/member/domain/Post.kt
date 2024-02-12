package com.teamsparta.member.domain

import com.teamsparta.member.dto.req.PostRequest
import com.teamsparta.member.dto.res.PostResponse
import com.teamsparta.member.global.entity.BaseEntity
import com.teamsparta.member.global.infra.config.CustomAuditorAware
import jakarta.persistence.*
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
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

    @Column(name = "created_by")
    override var createdBy: String,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @NotFound(action = NotFoundAction.IGNORE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    val member: Member,

    ): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    var id: Long? = null

    init {
        if (this.title.isEmpty()  || this.title.length > 500) {
            throw IllegalArgumentException()
        }
        if (this.content.isEmpty() || this.content.length > 5000)
            throw IllegalArgumentException()
    }


    fun update(request: PostRequest, authenticatedId: Long) {
        if (verify(authenticatedId)) {
            title = request.title
            content = request.content
            updatedAt = this.updatedAt
            updatedBy = CustomAuditorAware().currentAuditor.get()
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
        content = this.content,
        createdAt = this.createdAt,
        createdBy = this.createdBy,
    )

    companion object {
        fun of(request: PostRequest, member: Member) = Post(
            title = request.title,
            content = request.content,
            createdBy = CustomAuditorAware().currentAuditor.get(),
            member = member
        )
    }

}
