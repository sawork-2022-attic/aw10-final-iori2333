package com.micropos.delivery.service

import com.micropos.delivery.models.DeliveryRecord
import com.micropos.delivery.models.Order
import com.micropos.delivery.models.OrderStatus
import com.micropos.delivery.repository.DeliveryRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class KoujiDeliveryService : DeliveryService {
  @Autowired
  lateinit var deliveryRepository: DeliveryRepository

  private final val logger = LoggerFactory.getLogger(javaClass)

  override fun getDeliveryInfo(orderId: String): Mono<DeliveryRecord> {
    logger.info("pos-delivery: Getting delivery info for order $orderId")
    return Mono.fromCallable { deliveryRepository.findRecordByOrderId(orderId) }
  }

  override fun createDelivery(order: Order) {
    logger.info("pos-delivery: Creating delivery for order ${order.id}")
    order.status = OrderStatus.Delivering
    val record = DeliveryRecord(order, "Kouji is delivering", Date().time)
    deliveryRepository.saveRecord(record)

  }

  override fun updateDelivery(deliveryRecord: DeliveryRecord) {
    logger.info("pos-delivery: Updating delivery for order ${deliveryRecord.order.id}")
    val order = deliveryRepository.findRecordByOrderId(deliveryRecord.order.id) ?: return
    order.status = deliveryRecord.status
    deliveryRepository.saveRecord(deliveryRecord)
  }

  override fun cancelDelivery(order: Order) {
    logger.info("pos-delivery: Cancelling delivery for order ${order.id}")
    deliveryRepository.findRecordByOrderId(order.id) ?: return
    order.status = OrderStatus.Canceled
    deliveryRepository.deleteRecordByOrderId(order.id)
  }

  override fun getAllDeliveries(): Flux<DeliveryRecord> {
    logger.info("pos-delivery: Getting all deliveries")
    return Flux.fromIterable(deliveryRepository.findAllRecords())
  }
}
