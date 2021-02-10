package i.learn.apioptimization.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class OrderControllerTest(
    @Autowired val restTemplate: TestRestTemplate
) {
    @Test
    fun getOrdersByLazyLoading() {
        // requires the setting 'open-in-view: true'
        val response = restTemplate.exchange<Orders>(
            "/api/n-to-many/v2/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(data.isNotEmpty())
        data.forEach { assertTrue(it.orderItems.isNotEmpty()) }

        // @note: refer (*ToMany annotation in Order::class) and (GetOrderResponse::class)
        // cause N+1 query problem
        // 1 = select from orders
        // N = member, delivery, orderItems (by the num of orders)
        // M = items (by the num of orderItems)
    }

    @Test
    fun getOrdersByCollectionFetchJoin() {
        val response = restTemplate.exchange<Orders>(
            "/api/n-to-many/v3/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(data.isNotEmpty())
        data.forEach { assertTrue(it.orderItems.isNotEmpty()) }

        // @note: refer (collection fetch join for JPA)
        // only 1 query be executed

        /*** distinct, collection fetch join
        select
            distinct order0_.order_id as order_id1_6_0_,
            member1_.member_id as member_i1_4_1_,
            delivery2_.delivery_id as delivery1_2_2_,
            orderitems3_.order_item_id as order_it1_5_3_,
            item4_.item_id as item_id2_3_4_,
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
            delivery2_.status as status5_2_2_,
            orderitems3_.count as count2_5_3_,
            orderitems3_.item_id as item_id4_5_3_,
            orderitems3_.order_id as order_id5_5_3_,
            orderitems3_.order_price as order_pr3_5_3_,
            orderitems3_.order_id as order_id5_5_0__,
            orderitems3_.order_item_id as order_it1_5_0__,
            item4_.name as name3_3_4_,
            item4_.price as price4_3_4_,
            item4_.stock_quantity as stock_qu5_3_4_,
            item4_.artist as artist6_3_4_,
            item4_.etc as etc7_3_4_,
            item4_.author as author8_3_4_,
            item4_.isbn as isbn9_3_4_,
            item4_.actor as actor10_3_4_,
            item4_.director as directo11_3_4_,
            item4_.dtype as dtype1_3_4_
        from
            orders order0_
        inner join
            member member1_
                on order0_.member_id=member1_.member_id
        inner join
            delivery delivery2_
                on order0_.delivery_id=delivery2_.delivery_id
        inner join
            order_item orderitems3_
                on order0_.order_id=orderitems3_.order_id
        inner join
            item item4_
                on orderitems3_.item_id=item4_.item_id
        ***/
    }

    @Test
    fun getOrdersByFetchJoinAndLazyLoadingBatchSize() {
        val offset = 0
        val limit = 100
        val response = restTemplate.exchange<Orders>(
            "/api/n-to-many/v3.1/orders?offset=${offset}&limit=${limit}",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(data.isNotEmpty())
        assertTrue(limit - offset >= data.size)
        data.forEach { assertTrue(it.orderItems.isNotEmpty()) }

        // @note: (fetch join for *-to-one relation, paging)
        //  && (lazy loading, batch size for *-to-many relation, optimize N+1 query problem)
        // only 3 queries be executed
        // 1 = orders with member, delivery
        // 1 = orderItems in (fk.order_id)
        // 1 = items in (fk.item_id)

        /*** order. offset=0 이면 query에서 생략됨
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
                on order0_.delivery_id=delivery2_.delivery_id limit ?
        ***/
        /*** order_item. note where-in
        select
            orderitems0_.order_id as order_id5_5_1_,
            orderitems0_.order_item_id as order_it1_5_1_,
            orderitems0_.order_item_id as order_it1_5_0_,
            orderitems0_.count as count2_5_0_,
            orderitems0_.item_id as item_id4_5_0_,
            orderitems0_.order_id as order_id5_5_0_,
            orderitems0_.order_price as order_pr3_5_0_
        from
            order_item orderitems0_
        where
            orderitems0_.order_id in (?, ?)
        ***/
        /*** item. note where-in
        select
            item0_.item_id as item_id2_3_0_,
            item0_.name as name3_3_0_,
            item0_.price as price4_3_0_,
            item0_.stock_quantity as stock_qu5_3_0_,
            item0_.artist as artist6_3_0_,
            item0_.etc as etc7_3_0_,
            item0_.author as author8_3_0_,
            item0_.isbn as isbn9_3_0_,
            item0_.actor as actor10_3_0_,
            item0_.director as directo11_3_0_,
            item0_.dtype as dtype1_3_0_
        from
            item item0_
        where
            item0_.item_id in (?, ?, ?, ?)
        ***/
    }
}