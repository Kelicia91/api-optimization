package i.learn.apioptimization.domain

import i.learn.apioptimization.controller.interfaces.CreateMemberRequest
import javax.persistence.*

@Entity
@Table(name = "member")
data class Member(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long? = null,

    var name: String = "",

    @Embedded
    var address: Address? = null,

    // @note: 1:n <-> n:1 관계 설명을 위해 추가됨. 설계가 부적절하다고 생각할 필요x.
    @OneToMany(mappedBy = "member")
    var orders: MutableList<Order> = mutableListOf()
) {
    companion object {
        fun of(request: CreateMemberRequest): Member {
            return Member(name = request.name)
        }
    }
}