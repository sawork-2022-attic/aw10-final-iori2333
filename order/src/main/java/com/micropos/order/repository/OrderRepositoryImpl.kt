package com.micropos.order.repository

import com.micropos.order.models.Order
import com.micropos.order.models.OrderStatus
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Repository
class OrderRepositoryImpl : OrderRepository {
  private val orders = ConcurrentHashMap<String, Order>()

  private val random = Random(42)

  override fun getNextOrderId(): String {
    while (true) {
      val orderId = random.nextLong().toString()
      if (!orders.containsKey(orderId)) {
        return orderId
      }
    }
  }

  override fun getAllOrders(): List<Order> {
    return orders.values.toList()
  }

  override fun getOrderById(id: String): Order? {
    return orders[id]
  }

  override fun saveOrder(order: Order): Order {
    orders[order.id] = order
    return order
  }

  override fun deleteOrder(id: String): Order? {
    return orders.remove(id)
  }

  override fun updateStatus(id: String, status: String) {
    orders[id]?.status = OrderStatus.valueOf(status)
  }
}
