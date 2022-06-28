package com.micropos.cart.repository

import com.micropos.cart.models.Cart

sealed interface CartRepository {
  fun getCart(cartId: String): Cart?

  fun createCart(): Cart
}
