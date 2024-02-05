package com.teamsparta.member.domain

import com.teamsparta.member.dto.UserRole
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class PostTest: BehaviorSpec ({
    // 테스트 준비 - 테스트를 실행하기 위한 데이터 준비(포스트 생성자를 실행하는데 필요한 정보들)
    Given("information of Post") {
        val id = null
        val title = "title"
        val content = "content"
        val member = Member("test@gmail.com","test", "test1234", null, UserRole.MEMBER)

        // 테스트 실행 - 포스트 생성자 실행
        When("execute Post constructor") {
            val result = Post(
                title = title,
                content = content,
                member = member,
            )

            // 테스트 결과 - result 가 예상한 것과 같이 잘 반환됐는지 확인 : 포스트가 잘 생성됐는지 확인함(왼쪽 == 오른쪽)
            Then("result should be expected") {
                result.id shouldBe id
                result.title shouldBe title
                result.content shouldBe content
                result.member shouldBe member
            }
        }

    }


})