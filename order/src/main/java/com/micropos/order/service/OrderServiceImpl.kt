package com.micropos.order.service

import com.micropos.order.dto.ItemDto
import com.micropos.order.models.Order
import com.micropos.order.models.OrderStatus
import com.micropos.order.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderServiceImpl : OrderService {
  private final val logger = LoggerFactory.getLogger(javaClass)

  @Autowired
  lateinit var orderRepository: OrderRepository

  @Autowired
  lateinit var streamBridge: StreamBridge

  override fun getAllOrders(): Flux<Order> {
    logger.info("pos-order: Fetch all orders")
    return Flux.fromIterable(orderRepository.getAllOrders())
  }

  override fun getOrderById(id: String): Mono<Order?> {
    logger.info("pos-order: Fetch order by id: $id")
    return Mono.fromCallable { orderRepository.getOrderById(id) }
  }

  override fun createOrder(items: List<ItemDto>): Mono<Order> {
    logger.info("pos-order: Create new order")
    return Mono.fromCallable {
      val order = orderRepository.saveOrder(
        Order(
          orderRepository.getNextOrderId(),
          items,
          OrderStatus.ToSend,
        )
      )
      logger.info("pos-order: sending to stream: ${order.id}")
      streamBridge.send("ordering", order)
      order
    }
  }

  override fun updateOrder(id: String, items: List<ItemDto>): Mono<Order?> {
    val order = orderRepository.getOrderById(id) ?: return Mono.empty()
    return Mono.fromCallable {
      val newOrder = Order(
        order.id,
        items,
        order.status,
      )
      orderRepository.saveOrder(newOrder)
      newOrder
    }
  }

  override fun deleteOrder(id: String): Mono<Order?> {
    val order = orderRepository.deleteOrder(id) ?: return Mono.empty()
    return Mono.fromCallable {
//      streamBridge.send("delete-order", order)
      order
    }
  }

  override fun updateOrderStatus(id: String, status: OrderStatus): Mono<Order?> {
    val order = orderRepository.getOrderById(id) ?: return Mono.empty()
    return Mono.fromCallable {
      val newOrder = Order(
        order.id,
        order.items,
        status,
      )
      orderRepository.saveOrder(newOrder)
      newOrder
    }
  }
}
