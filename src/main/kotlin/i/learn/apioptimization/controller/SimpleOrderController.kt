package i.learn.apioptimization.controller

import i.learn.apioptimization.controller.interfaces.GetSimpleOrderResponse
import i.learn.apioptimization.controller.interfaces.WrappedResponse
import i.learn.apioptimization.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/n-to-one")
class SimpleOrderController(
    private val orderService: OrderService
) {
    @GetMapping("/v2/orders")
    fun getOrdersByLazyLoading(): ResponseEntity<WrappedResponse<List<GetSimpleOrderResponse>>> {
        val orders = orderService.getOrdersByFindAll()
        return ResponseEntity.ok(
            WrappedResponse.of(orders.map { GetSimpleOrderResponse.of(it) })
        )
    }

    @GetMapping("/v3/orders")
    fun getOrdersByFetchJoin(): ResponseEntity<WrappedResponse<List<GetSimpleOrderResponse>>> {
        val orders = orderService.getOrdersByFetchJoin()
        return ResponseEntity.ok(
            WrappedResponse.of(orders.map { GetSimpleOrderResponse.of(it) })
        )
    }

    @GetMapping("/v4/orders")
    fun getOrdersByResponse(): ResponseEntity<WrappedResponse<List<GetSimpleOrderResponse>>> {
        val orderResponses = orderService.getOrderResponses()
        return ResponseEntity.ok(
            WrappedResponse.of(orderResponses)
        )
    }
}