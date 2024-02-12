package com.teamsparta.member.domain

import com.teamsparta.member.dto.UserRole
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.util.UUID

class PostTest: BehaviorSpec ({
    Given("information of Post") {
        val id = null
        val title = "title"
        val content = "content"
        val createdBy = "test@gmail.com"
        val member = Member(email = "test@gmail.com", nickName = "test", password = "1234", refreshToken = "", isDeleted = false, role = UserRole.MEMBER, createdAt = LocalDateTime.now())

        When("execute Post constructor") {
            val result = Post(
                title = title,
                content = content,
                createdBy = createdBy,
                member = member,
            )

            Then("result should be expected") {
                result.id shouldBe id
                result.title shouldBe title
                result.content shouldBe content
                result.member shouldBe member
            }
        }
    }

    Given("information of Post with empty title") {
        val title = ""
        val content = "content"
        val createdBy = "test@gmail.com"
        val member = Member(email = "test@gmail.com", nickName = "test", password = "1234", refreshToken = "", isDeleted = false, role = UserRole.MEMBER, createdAt = LocalDateTime.now())

        When("execute Post constructor") {
            val result = shouldThrow<IllegalArgumentException> {
                Post(
                    title = title,
                    content = content,
                    createdBy = createdBy,
                    member = member,
                )
            }

            Then("thrown exception message should be expected") {
                result.message shouldBe null
            }
        }
    }


    Given("information of Post with empty content") {
        val title = "title"
        val content = ""
        val createdBy = "test@gmail.com"
        val member = Member(email = "test@gmail.com", nickName = "test", password = "1234", refreshToken = "", isDeleted = false, role = UserRole.MEMBER, createdAt = LocalDateTime.now())

        When("execute Post constructor") {
            val result = shouldThrow<IllegalArgumentException> {
                Post(
                    title = title,
                    content = content,
                    createdBy = createdBy,
                    member = member,
                )
            }

            Then("thrown exception message should be expected") {
                result.message shouldBe null
            }
        }
    }

    Given("information of Post title with over 500 characters") {
        val title = (0..500).map { UUID.randomUUID().toString().replace("-","") }.joinToString()
        val content = "content"
        val createdBy = "test@gmail.com"
        val member = Member(email = "test@gmail.com", nickName = "test", password = "1234", refreshToken = "", isDeleted = false, role = UserRole.MEMBER, createdAt = LocalDateTime.now())

        When("execute Post constructor") {
            val result = shouldThrow<IllegalArgumentException> {
                Post(
                    title = title,
                    content = content,
                    createdBy = createdBy,
                    member = member,
                )
            }

            Then("thrown exception message should be expected") {
                result.message shouldBe null
            }
        }
    }

    Given("information of Post content with over 5000 characters") {
        val title = "title"
        val content = (0..5000).map { UUID.randomUUID().toString().replace("-","") }.joinToString()
        val createdBy = "test@gmail.com"
        val member = Member(email = "test@gmail.com", nickName = "test", password = "1234", refreshToken = "", isDeleted = false, role = UserRole.MEMBER, createdAt = LocalDateTime.now())

        When("execute Post constructor") {
            val result = shouldThrow<IllegalArgumentException> {
                Post(
                    title = title,
                    content = content,
                    createdBy = createdBy,
                    member = member,
                )
            }

            Then("thrown exception message should be expected") {
                result.message shouldBe null
            }
        }
    }

})