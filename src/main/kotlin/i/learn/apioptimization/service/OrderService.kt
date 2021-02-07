package i.learn.apioptimization.service

import i.learn.apioptimization.controller.interfaces.GetOrderResponse
import i.learn.apioptimization.domain.Order
import i.learn.apioptimization.repository.OrderRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val em: EntityManager
) {
    fun getOrdersByFindAll(): List<Order> {
        return orderRepository.findAll()
    }

    fun getOrdersByFetchJoin(): List<Order> {
        return em.createQuery(
    "select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d"
            , Order::class.java
        ).resultList
    }

    fun getOrderResponses(): List<GetOrderResponse> {
        return em.createQuery(
    "select new i.learn.apioptimization.controller.interfaces.GetOrderResponse(o.id, m.name, o.orderedAt, o.status, d.address)" +
            " from Order o" +
            " join o.member m" +
            " join o.delivery d"
            , GetOrderResponse::class.java
        ).resultList
    }
}