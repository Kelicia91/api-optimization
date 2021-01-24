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
    @JoinColumn(name = "item_id")
    val item: Item? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null,

    val orderPrice: Int = 0, //주문 가격

    val count: Int = 0 //주문 수량
)
