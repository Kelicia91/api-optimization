package i.learn.apioptimization.controller

import i.learn.apioptimization.controller.interfaces.GetOrderResponse
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

typealias Orders = WrappedResponse<List<GetOrderResponse>>

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
}