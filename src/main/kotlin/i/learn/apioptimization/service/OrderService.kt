package i.learn.apioptimization.service

import i.learn.apioptimization.controller.interfaces.GetSimpleOrderResponse
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

    fun getOrderResponses(): List<GetSimpleOrderResponse> {
        // @note: string query 사용 하니까 rename class 해도 반영 안 되서 오류 난다!
        return em.createQuery(
    "select new i.learn.apioptimization.controller.interfaces.GetSimpleOrderResponse(o.id, m.name, o.orderedAt, o.status, d.address)" +
            " from Order o" +
            " join o.member m" +
            " join o.delivery d"
            , GetSimpleOrderResponse::class.java
        ).resultList
    }

    /*** @note: recommend QueryDSL to replace sql-string ***/

    fun getOrdersByCollectionFetchJoin(): List<Order> {
        /***
         * @note: 'distinct' & '1:N fetch join'
         * db join 결과를 보면 (order:item=1:many) 관계로 인해 1 order에 대해 many 만큼 row가 반환 되기 때문에
         * 중복된 object order가 생성된다. 그래서 distinct를 사용해 중복 제거.
         */
        return em.createQuery(
    "select distinct o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch oi.item i"
            , Order::class.java
        )
// @note: offset, limit 설정해도 db-query 반영 안됨
// @warn: WARN 36019 --- [o-auto-1-exec-1] o.h.h.internal.ast.QueryTranslatorImpl   : HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
//        .setFirstResult(1)    // offset
//        .setMaxResults(100)   // limit
        .resultList
    }
}