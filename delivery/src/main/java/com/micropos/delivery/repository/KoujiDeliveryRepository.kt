package com.micropos.delivery.repository

import com.micropos.delivery.models.DeliveryRecord
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class KoujiDeliveryRepository : DeliveryRepository {
  private val deliveries = ConcurrentHashMap<String, DeliveryRecord>()

  override fun saveRecord(delivery: DeliveryRecord): DeliveryRecord {
    deliveries[delivery.order.id] = delivery
    return delivery
  }

  override fun findRecordByOrderId(orderId: String): DeliveryRecord? {
    return deliveries[orderId]
  }

  override fun findAllRecords(): List<DeliveryRecord> {
    return deliveries.values.toList()
  }

  override fun deleteRecordByOrderId(orderId: String): DeliveryRecord? {
    return deliveries.remove(orderId)
  }
}
