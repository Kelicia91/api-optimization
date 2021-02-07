package i.learn.apioptimization.controller

import i.learn.apioptimization.InitDb
import i.learn.apioptimization.controller.interfaces.GetOrderResponse
import i.learn.apioptimization.controller.interfaces.WrappedResponse
import i.learn.apioptimization.domain.*
import i.learn.apioptimization.repository.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerTest(
    @Autowired val restTemplate: TestRestTemplate
) {
    @Autowired lateinit var memberRepository: MemberRepository
    @Autowired lateinit var itemRepository: ItemRepository
    @Autowired lateinit var orderRepository: OrderRepository

    @MockBean lateinit var initDb: InitDb

    private lateinit var orders: List<Order>

    @BeforeAll
    fun beforeAll() {
        orderRepository.deleteAll()
        itemRepository.deleteAll()
        memberRepository.deleteAll()

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
        orders = orderRepository.saveAll(listOf(
            Order(
                member = members[0],
                delivery = Delivery(address = members[0].address),
                orderItems = mutableListOf(
                    OrderItem(item = items[0], orderPrice = 10000, count = 1),
                    OrderItem(item = items[1], orderPrice = 10000, count = 2),
                )
            ),
            Order(
                member = members[1],
                delivery = Delivery(address = members[1].address),
                orderItems = mutableListOf(
                    OrderItem(item = items[2], orderPrice = 10000, count = 3),
                    OrderItem(item = items[3], orderPrice = 10000, count = 4),
                )
            ),
        ))
    } // beforeAll

    @Test
    fun getOrdersByLazyLoading() {
        val response = restTemplate.exchange<WrappedResponse<List<GetOrderResponse>>>(
            "/api/v2/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(orders.size <= data.size)

        // @note: refer (*ToOne annotation in Order::class) and (GetOrderResponse::class)
        // cause N+1 query problem
        // 1 = select from orders
        // N = select from member, delivery (by the num of orders)

        /*** order (N=2)
        select
            order0_.order_id as order_id1_6_,
            order0_.delivery_id as delivery4_6_,
            order0_.member_id as member_i5_6_,
            order0_.ordered_at as ordered_2_6_,
            order0_.status as status3_6_
        from
            orders order0_
        ***/
        /*** member
        select
            member0_.member_id as member_i1_4_0_,
            member0_.city as city2_4_0_,
            member0_.street as street3_4_0_,
            member0_.zipcode as zipcode4_4_0_,
            member0_.name as name5_4_0_
        from
            member member0_
        where
            member0_.member_id=?
        ***/
        /*** delivery
        select
            delivery0_.delivery_id as delivery1_2_0_,
            delivery0_.city as city2_2_0_,
            delivery0_.street as street3_2_0_,
            delivery0_.zipcode as zipcode4_2_0_,
            delivery0_.status as status5_2_0_
        from
            delivery delivery0_
        where
            delivery0_.delivery_id=?
        ***/
        // repeat member, delivery
    }

    @Test
    fun getOrdersByFetchJoin() {
        val response = restTemplate.exchange<WrappedResponse<List<GetOrderResponse>>>(
            "/api/v3/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(orders.size <= data.size)

        // @note: refer (fetch join for JPA)
        // only 1 query be executed

        /*** fetch join
        select
            order0_.order_id as order_id1_6_0_,
            member1_.member_id as member_i1_4_1_,
            delivery2_.delivery_id as delivery1_2_2_,
            order0_.delivery_id as delivery4_6_0_,
            order0_.member_id as member_i5_6_0_,
            order0_.ordered_at as ordered_2_6_0_,
            order0_.status as status3_6_0_,
            member1_.city as city2_4_1_,
            member1_.street as street3_4_1_,
            member1_.zipcode as zipcode4_4_1_,
            member1_.name as name5_4_1_,
            delivery2_.city as city2_2_2_,
            delivery2_.street as street3_2_2_,
            delivery2_.zipcode as zipcode4_2_2_,
            delivery2_.status as status5_2_2_
        from
            orders order0_
        inner join
            member member1_
                on order0_.member_id=member1_.member_id
        inner join
            delivery delivery2_
                on order0_.delivery_id=delivery2_.delivery_id
        ***/
    }

    @Test
    fun getOrdersByResponse() {
        val response = restTemplate.exchange<WrappedResponse<List<GetOrderResponse>>>(
            "/api/v4/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(orders.size <= data.size)

        // @note: query exactly by api response specification
        // only 1 query be executed

        /*** direct-query by response
        select
            order0_.order_id as col_0_0_,
            member1_.name as col_1_0_,
            order0_.ordered_at as col_2_0_,
            order0_.status as col_3_0_,
            delivery2_.city as col_4_0_,
            delivery2_.street as col_4_1_,
            delivery2_.zipcode as col_4_2_
        from
            orders order0_
        inner join
            member member1_
                on order0_.member_id=member1_.member_id
        inner join
            delivery delivery2_
                on order0_.delivery_id=delivery2_.delivery_id
        ***/
    }
}