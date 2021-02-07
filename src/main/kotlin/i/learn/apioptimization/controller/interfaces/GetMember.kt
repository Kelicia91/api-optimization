package i.learn.apioptimization.controller.interfaces

import i.learn.apioptimization.domain.Member

class GetMemberResponse(
    val name: String
) {
    companion object {
        fun of(member: Member): GetMemberResponse {
            return GetMemberResponse(name = member.name)
        }
    }
}