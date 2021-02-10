package i.learn.apioptimization.controller

import i.learn.apioptimization.controller.interfaces.GetOrderResponse
import i.learn.apioptimization.controller.interfaces.WrappedResponse
import i.learn.apioptimization.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/n-to-many")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping("/v2/orders")
    fun getOrdersByLazyLoading(): ResponseEntity<WrappedResponse<List<GetOrderResponse>>> {
        val orders = orderService.getOrdersByFindAll()
        return ResponseEntity.ok(
            WrappedResponse(orders.map(GetOrderResponse::of))
        )
    }
}