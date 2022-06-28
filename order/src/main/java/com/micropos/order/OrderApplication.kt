package com.micropos.order

import com.micropos.order.models.Order
import com.micropos.order.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean
import java.util.function.Consumer

@SpringBootApplication
@EnableDiscoveryClient
class OrderApplication {
  @Autowired
  private lateinit var orderService: OrderService

  @Bean
  fun onOrderCreated(): Consumer<Order> {
    return Consumer { order ->
      orderService
        .updateOrder(order.id, order.items)
        .block()
    }
  }
}

fun main(args: Array<String>) {
  runApplication<OrderApplication>(*args)
}
