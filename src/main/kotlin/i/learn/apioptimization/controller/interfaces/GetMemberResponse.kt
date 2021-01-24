package i.learn.apioptimization.controller.interfaces

import i.learn.apioptimization.domain.Member

data class WrappedView<T>(
    val data: T
) {
    companion object {
        fun <T> of(value: T): WrappedView<T> {
            return WrappedView(data = value)
        }
    }
}

class GetMemberResponse(
    val name: String
) {
    companion object {
        fun of(member: Member): GetMemberResponse {
            return GetMemberResponse(name = member.name)
        }
    }
}