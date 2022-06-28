package com.micropos.cart.repository

import com.micropos.cart.models.Cart
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Repository
class CartRepositoryImpl : CartRepository {
  private val carts = ConcurrentHashMap<String, Cart>()

  private val random = Random(42)

  private fun getNextCartId(): String {
    while (true) {
      val id = random.nextLong().toString()
      if (!carts.containsKey(id)) {
        return id
      }
    }
  }

  override fun getCart(cartId: String): Cart? {
    return carts[cartId]
  }

  override fun createCart(): Cart {
    val id = getNextCartId()
    val cart = Cart(id, mutableListOf())
    carts[id] = cart
    return cart
  }
}
