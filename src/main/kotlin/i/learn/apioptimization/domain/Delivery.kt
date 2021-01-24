package i.learn.apioptimization.domain

import javax.persistence.*

@Entity
data class Delivery(
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    var id: Long? = null,

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order: Order? = null,

    @Embedded
    val address: Address? = null,

    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus? = null
)
