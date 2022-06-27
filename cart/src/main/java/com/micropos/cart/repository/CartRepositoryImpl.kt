package com.micropos.cart.repository

import com.micropos.cart.models.Cart
import org.springframework.stereotype.Repository

@Repository
class CartRepositoryImpl : CartRepository {
  // TODO: support cart for multiple users
  private final val cart = Cart()

  override fun getCart(): Cart {
    return cart
  }
}
