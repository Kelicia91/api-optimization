package i.learn.apioptimization.controller

import i.learn.apioptimization.controller.interfaces.GetSimpleOrderResponse
import i.learn.apioptimization.controller.interfaces.WrappedResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

typealias SimpleOrders = WrappedResponse<List<GetSimpleOrderResponse>>

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimpleOrderControllerTest(
    @Autowired val restTemplate: TestRestTemplate
) {
    @Test
    fun getOrdersByLazyLoading() {
        // requires the setting 'open-in-view: true'
        val response = restTemplate.exchange<SimpleOrders>(
            "/api/n-to-one/v2/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(data.isNotEmpty())

        // @note: refer (*ToOne annotation in Order::class) and (GetSimpleOrderResponse::class)
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
        val response = restTemplate.exchange<SimpleOrders>(
            "/api/n-to-one/v3/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(data.isNotEmpty())

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
        val response = restTemplate.exchange<SimpleOrders>(
            "/api/n-to-one/v4/orders",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(data.isNotEmpty())

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
