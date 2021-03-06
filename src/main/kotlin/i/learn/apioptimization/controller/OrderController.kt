package i.learn.apioptimization.controller

import i.learn.apioptimization.controller.interfaces.GetOrderResponse
import i.learn.apioptimization.controller.interfaces.WrappedResponse
import i.learn.apioptimization.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

typealias Orders = WrappedResponse<List<GetOrderResponse>>

@RestController
@RequestMapping("/api/n-to-many")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping("/v2/orders")
    fun getOrdersByLazyLoading(): ResponseEntity<Orders> {
        val orders = orderService.getOrdersByFindAll()
        return ResponseEntity.ok(
            WrappedResponse(orders.map(GetOrderResponse::of))
        )
    }

    @GetMapping("/v3/orders")
    fun getOrdersByCollectionFetchJoin(): ResponseEntity<Orders> {
        val orders = orderService.getOrdersByCollectionFetchJoin()
        return ResponseEntity.ok(
            WrappedResponse(orders.map(GetOrderResponse::of))
        )
    }

    @GetMapping("/v3.1/orders")
    fun getOrdersByFetchJoinAndLazyLoadingBatchSize(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "100") limit: Int,
    ): ResponseEntity<Orders> {
        val orders = orderService.getOrdersByFetchJoin(offset, limit)
        return ResponseEntity.ok(
            WrappedResponse(orders.map(GetOrderResponse::of))
        )
    }

    @GetMapping("/v4/orders")
    fun getOrdersByResponse(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "100") limit: Int,
    ): ResponseEntity<Orders> {
        val orderViews = orderService.getOrderResponses(offset, limit)
        return ResponseEntity.ok(WrappedResponse(orderViews))
    }
}