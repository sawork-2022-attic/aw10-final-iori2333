package com.micropos.cart.repository

import com.micropos.cart.models.Cart

sealed interface CartRepository {
  fun getCart(): Cart
}
