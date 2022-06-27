package com.micropos.delivery.controller

import com.micropos.delivery.api.DeliveryApi
import com.micropos.delivery.dto.DeliveryRecordDto
import com.micropos.delivery.mapper.DeliveryMapper
import com.micropos.delivery.service.DeliveryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/api")
class DeliveryController : DeliveryApi {
  @Autowired
  lateinit var deliveryService: DeliveryService

  @Autowired
  lateinit var deliveryMapper: DeliveryMapper

  override fun getDeliveryInfo(exchange: ServerWebExchange?): Mono<ResponseEntity<Flux<DeliveryRecordDto>>> {
    val flux = deliveryService
      .getAllDeliveries()
      .map { deliveryMapper.toDeliveryRecordDto(it) }
    return Mono.just(ResponseEntity.ok(flux))
  }

  override fun getOrderDeliveryInfo(
    orderId: String?,
    exchange: ServerWebExchange?
  ): Mono<ResponseEntity<DeliveryRecordDto>> {
    if (orderId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return deliveryService.getDeliveryInfo(orderId)
      .map { deliveryMapper.toDeliveryRecordDto(it) }
      .map { ResponseEntity.ok(it) }
      .defaultIfEmpty(ResponseEntity.notFound().build())
  }
}
