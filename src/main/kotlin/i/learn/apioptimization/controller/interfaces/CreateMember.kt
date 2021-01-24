package i.learn.apioptimization.controller.interfaces

import i.learn.apioptimization.domain.Member
import i.learn.apioptimization.exception.AppException
import i.learn.apioptimization.support.MessageKey
import javax.validation.constraints.NotBlank

// DTO

data class CreateMemberRequest(
    @NotBlank val name: String
)

data class CreatedMemberResponse(
    val id: Long,
    val name: String
) {
    companion object {
        fun of(member: Member): CreatedMemberResponse {
            return CreatedMemberResponse(
            member.id ?: throw AppException(MessageKey.EXCEPTION),
                member.name
            )
        }
    }
}