package i.learn.apioptimization.domain

import javax.persistence.*

@Entity
@Table(name = "order_item")
data class OrderItem(
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    val item: Item,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    val orderPrice: Int = 0, //주문 가격

    val count: Int = 0 //주문 수량
)
