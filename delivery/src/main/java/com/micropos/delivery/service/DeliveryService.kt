package com.micropos.delivery.service

import com.micropos.delivery.models.DeliveryRecord
import com.micropos.delivery.models.Order
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

sealed interface DeliveryService {
  fun getDeliveryInfo(orderId: String): Mono<DeliveryRecord>

  fun createDelivery(order: Order)

  fun updateDelivery(deliveryRecord: DeliveryRecord)

  fun cancelDelivery(order: Order)

  fun getAllDeliveries(): Flux<DeliveryRecord>
}
