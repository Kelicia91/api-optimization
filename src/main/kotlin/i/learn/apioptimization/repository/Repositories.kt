package i.learn.apioptimization.repository

import i.learn.apioptimization.domain.*
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long>

interface ItemRepository: JpaRepository<Item, Long>

interface OrderRepository: JpaRepository<Order, Long>