package com.micropos.order.repository

import com.micropos.order.models.Order

sealed interface OrderRepository {
  fun getNextOrderId(): String
  fun getAllOrders(): List<Order>
  fun getOrderById(id: String): Order?
  fun saveOrder(order: Order): Order
  fun deleteOrder(id: String): Order?
  fun updateStatus(id: String, status: String)
}
