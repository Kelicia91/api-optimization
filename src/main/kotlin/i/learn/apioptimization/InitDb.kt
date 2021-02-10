package i.learn.apioptimization

import i.learn.apioptimization.domain.*
import i.learn.apioptimization.repository.ItemRepository
import i.learn.apioptimization.repository.MemberRepository
import i.learn.apioptimization.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class InitDb(
    @Autowired private val initService: InitService
) {
    @PostConstruct
    fun init() {
        initService.addSample()
    }

    @Component
    @Transactional
    class InitService(
        @Autowired private val memberRepository: MemberRepository,
        @Autowired private val itemRepository: ItemRepository,
        @Autowired private val orderRepository: OrderRepository,
    ) {
        fun addSample() {
            val members = memberRepository.saveAll(listOf(
                Member(name = "alice", address = Address(city = "서울", street = "1", zipcode = "123")),
                Member(name = "bob", address = Address(city = "진주", street = "2", zipcode = "456"))
            ))
            val items = itemRepository.saveAll(listOf(
                Book(name = "JPA1 BOOK", price = 10000, stockQuantity = 100),
                Book(name = "JPA2 BOOK", price = 20000, stockQuantity = 200),
                Book(name = "SPRING1 BOOK", price = 30000, stockQuantity = 300),
                Book(name = "SPRING2 BOOK", price = 40000, stockQuantity = 400)
            ))
            orderRepository.saveAll(listOf(
                Order(
                    member = members[0],
                    delivery = Delivery(address = members[0].address),
                ).also {
                    it.orderItems = listOf(
                        OrderItem(item = items[0], orderPrice = 10000, count = 1, order = it),
                        OrderItem(item = items[1], orderPrice = 10000, count = 2, order = it),
                    )
                },
                Order(
                    member = members[1],
                    delivery = Delivery(address = members[1].address),
                ).also {
                    it.orderItems = listOf(
                        OrderItem(item = items[2], orderPrice = 10000, count = 3, order = it),
                        OrderItem(item = items[3], orderPrice = 10000, count = 4, order = it),
                    )
                }
            ))
        }
    }
}