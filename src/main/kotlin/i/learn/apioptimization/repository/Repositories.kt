package i.learn.apioptimization.repository

import i.learn.apioptimization.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long>