package i.learn.apioptimization.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "orders") // @note: DB의 'order by' 예약어 때문에 관례를 따름
data class Order(
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val orderItems: MutableList<OrderItem> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    var delivery: Delivery? = null,

    val orderedAt: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var status: OrderStatus? = null
) {
    //==연관관계 메서드==//
    fun set(member: Member) {
        this.member = member
        member.orders.add(this)
    }

    fun addOrderItem(orderItem: OrderItem) {
        orderItems.add(orderItem)
        orderItem.order = this
    }

    fun set(delivery: Delivery) {
        this.delivery = delivery
        delivery.order = this
    }
}