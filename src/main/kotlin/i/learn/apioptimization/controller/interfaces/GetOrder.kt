package i.learn.apioptimization.controller.interfaces

import i.learn.apioptimization.domain.Address
import i.learn.apioptimization.domain.Order
import i.learn.apioptimization.domain.OrderItem
import i.learn.apioptimization.domain.OrderStatus
import i.learn.apioptimization.exception.AppException
import i.learn.apioptimization.support.MessageKey
import java.time.LocalDateTime

data class GetOrderResponse(
    val orderId: Long,
    val name: String,
    val orderedAt: LocalDateTime,
    val orderStatus: OrderStatus,
    val deliveryAddress: Address?,
    val orderItems: List<GetOrderItemResponse>
) {
    companion object {
        fun of(order: Order): GetOrderResponse {
            return GetOrderResponse(
                orderId = order.id ?: throw AppException(MessageKey.EXCEPTION),
                name = order.member.name,
                orderedAt = order.orderedAt ?: throw AppException(MessageKey.EXCEPTION),
                orderStatus = order.status,
                deliveryAddress = order.delivery?.address,
                orderItems = order.orderItems.map(GetOrderItemResponse::of) // one-to-many
            )
        }
    }
}

data class GetOrderItemResponse(
    val name: String,
    val orderPrice: Int,
    val count: Int
) {
    companion object {
        fun of(orderItem: OrderItem): GetOrderItemResponse {
            return GetOrderItemResponse(
                name = orderItem.item.name,
                orderPrice = orderItem.orderPrice,
                count = orderItem.count
            )
        }
    }
}