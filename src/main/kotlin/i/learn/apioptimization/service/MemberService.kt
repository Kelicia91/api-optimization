package i.learn.apioptimization.service

import i.learn.apioptimization.domain.Member
import i.learn.apioptimization.exception.AppException
import i.learn.apioptimization.exception.NotFoundException
import i.learn.apioptimization.repository.MemberRepository
import i.learn.apioptimization.support.MessageKey
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun add(member: Member): Member {
        return memberRepository.save(member)
    }

    fun get(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { NotFoundException(MessageKey.EXCEPTION) }
    }

    fun get(): List<Member> {
        return memberRepository.findAll()
    }

    @Transactional
    fun update(id: Long, name: String): Long {
        val member = memberRepository.findById(id)
            .orElseThrow { NotFoundException(MessageKey.EXCEPTION) }
        member.apply { this.name = name }
        return member.id ?: throw AppException(MessageKey.EXCEPTION)
    }
}
