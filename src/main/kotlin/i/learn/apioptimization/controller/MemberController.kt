package i.learn.apioptimization.controller

import i.learn.apioptimization.controller.interfaces.CreateMemberRequest
import i.learn.apioptimization.controller.interfaces.CreatedMemberResponse
import i.learn.apioptimization.controller.interfaces.UpdateMemberRequest
import i.learn.apioptimization.controller.interfaces.UpdatedMemberResponse
import i.learn.apioptimization.domain.Member
import i.learn.apioptimization.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

import javax.validation.Valid

@RestController
@RequestMapping("/members")
class MemberController(
    @Autowired
    private val memberService: MemberService
) {
    @PostMapping
    fun create(@RequestBody @Valid request: CreateMemberRequest): ResponseEntity<CreatedMemberResponse> {
        val member = memberService.add(Member.of(request))
        return ResponseEntity.created(URI.create("/members/${member.id}"))
                .body(CreatedMemberResponse.of(member))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: UpdateMemberRequest
    ): ResponseEntity<UpdatedMemberResponse> {
        val memberId = memberService.update(id, request.name)  // updated member 반환해도 되지만 command와 query를 분리하기 위해 반환 x
        val member = memberService.get(memberId)      // 갱신이 자주 일어나거나 성능이 중요한 경우 update 에서 반환할 수도 있음
        return ResponseEntity.ok(UpdatedMemberResponse.of(member))
    }
}

