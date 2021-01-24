package i.learn.apioptimization.controller.interfaces

import i.learn.apioptimization.domain.Member
import i.learn.apioptimization.exception.AppException
import i.learn.apioptimization.support.MessageKey

// DTO

data class UpdateMemberRequest(
    val name: String
)

data class UpdatedMemberResponse(
    val id: Long,
    val name: String
) {
    companion object {
        fun of(member: Member): UpdatedMemberResponse {
            return UpdatedMemberResponse(
                id = member.id ?: throw AppException(MessageKey.EXCEPTION),
                name = member.name
            )
        }
    }
}