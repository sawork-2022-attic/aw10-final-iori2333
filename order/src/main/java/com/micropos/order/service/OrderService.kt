package com.micropos.order.service

import com.micropos.order.dto.ItemDto
import com.micropos.order.models.Order
import com.micropos.order.models.OrderStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

sealed interface OrderService {
  fun getAllOrders(): Flux<Order>
  fun getOrderById(id: String): Mono<Order?>
  fun createOrder(items: List<ItemDto>): Mono<Order>
  fun updateOrder(id: String, items: List<ItemDto>): Mono<Order?>
  fun deleteOrder(id: String): Mono<Order?>
  fun updateOrderStatus(id: String, status: OrderStatus): Mono<Order?>
}
