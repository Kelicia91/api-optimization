package i.learn.apioptimization.controller.interfaces

import i.learn.apioptimization.domain.Address
import i.learn.apioptimization.domain.Order
import i.learn.apioptimization.domain.OrderStatus
import i.learn.apioptimization.exception.AppException
import i.learn.apioptimization.support.MessageKey
import java.time.LocalDateTime

data class GetSimpleOrderResponse(
    val orderId: Long,
    val name: String,
    val orderedAt: LocalDateTime,
    val orderStatus: OrderStatus,
    val deliveryAddress: Address?
) {
    companion object {
        fun of(order: Order): GetSimpleOrderResponse {
            return GetSimpleOrderResponse(
                orderId = order.id ?: throw AppException(MessageKey.EXCEPTION),
                name = order.member.name,
                orderedAt = order.orderedAt ?: throw AppException(MessageKey.EXCEPTION),
                orderStatus = order.status,
                deliveryAddress = order.delivery?.address
            )
        }
    }
}