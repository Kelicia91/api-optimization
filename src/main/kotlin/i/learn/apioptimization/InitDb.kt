package i.learn.apioptimization

import i.learn.apioptimization.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Component
class InitDb {
    @Autowired
    private val initService: InitService? = null

    @PostConstruct
    fun init() {
        initService?.dbInit1()
        initService?.dbInit2()
    }

    @Component
    @Transactional
    internal class InitService {
        @Autowired
        private val em: EntityManager? = null

        fun dbInit1() {
            val member = Member(name = "userA", address = Address(city = "서울", street = "1", zipcode = "1111"))
            em?.persist(member)
            val book1 = Book(name = "JPA1 BOOK", price = 10000, stockQuantity = 100)
            em?.persist(book1)
            val book2 = Book(name ="JPA2 BOOK", price =20000, stockQuantity = 100)
            em?.persist(book2)
            val orderItem1 = OrderItem(item = book1, orderPrice = 10000, count = 1)
            val orderItem2 = OrderItem(item = book2, orderPrice = 10000, count = 2)
            val order = Order(member = member, delivery = Delivery(address = member.address),
                orderItems = mutableListOf(orderItem1, orderItem2))
            em?.persist(order)
        }

        fun dbInit2() {
            val member = Member(name = "userB", address = Address(city = "진주", street = "2", zipcode = "2222"))
            em?.persist(member)
            val book1 = Book(name ="SPRING1 BOOK", price = 20000, stockQuantity = 200)
            em?.persist(book1)
            val book2 = Book(name = "SPRING2 BOOK", price = 40000, stockQuantity = 300)
            em?.persist(book2)
            val orderItem1 = OrderItem(item = book1, orderPrice = 20000, count = 3)
            val orderItem2 = OrderItem(item = book2, orderPrice = 40000, count = 4)
            val order = Order(member = member, delivery = Delivery(address = member.address),
                orderItems = mutableListOf(orderItem1, orderItem2))
            em?.persist(order)
        }
    }
}