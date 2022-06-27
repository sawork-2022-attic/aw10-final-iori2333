package com.micropos.order.controller

import com.micropos.order.api.OrderApi
import com.micropos.order.dto.ItemDto
import com.micropos.order.dto.OrderDto
import com.micropos.order.mapper.OrderMapper
import com.micropos.order.models.OrderStatus
import com.micropos.order.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Controller
@RequestMapping("/api")
class OrderController : OrderApi {
  @Autowired
  lateinit var orderService: OrderService

  @Autowired
  lateinit var orderMapper: OrderMapper

  override fun createOrder(itemDto: Flux<ItemDto>?, exchange: ServerWebExchange?): Mono<ResponseEntity<OrderDto>> {
    if (itemDto == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return itemDto
      .collect(Collectors.toList())
      .flatMap { orderService.createOrder(it) }
      .map { orderMapper.toOrderDto(it) }
      .map { ResponseEntity.ok(it) }
  }

  override fun getAllOrders(exchange: ServerWebExchange?): Mono<ResponseEntity<Flux<OrderDto>>> {
    val flux = orderService.getAllOrders()
      .map { orderMapper.toOrderDto(it) }
    return Mono.just(ResponseEntity.ok(flux))
  }

  override fun getOrderById(orderId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<OrderDto>> {
    if (orderId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return orderService.getOrderById(orderId)
      .map { orderMapper.toOrderDto(it) }
      .map { ResponseEntity.ok(it) }
      .defaultIfEmpty(ResponseEntity.notFound().build())
  }

  override fun removeOrderById(orderId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<OrderDto>> {
    if (orderId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return orderService.deleteOrder(orderId)
      .map { orderMapper.toOrderDto(it) }
      .map { ResponseEntity.ok(it) }
      .defaultIfEmpty(ResponseEntity.notFound().build())
  }

  override fun updateOrderStatusById(
    orderId: String?,
    status: String?,
    exchange: ServerWebExchange?
  ): Mono<ResponseEntity<OrderDto>> {
    if (orderId == null || status == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }

    val statusEnum = try {
      OrderStatus.valueOf(status)
    } catch (e: IllegalArgumentException) {
      return Mono.just(ResponseEntity.badRequest().build())
    }

    return orderService.updateOrderStatus(orderId, statusEnum)
      .map { orderMapper.toOrderDto(it) }
      .map { ResponseEntity.ok(it) }
      .defaultIfEmpty(ResponseEntity.notFound().build())
  }
}
