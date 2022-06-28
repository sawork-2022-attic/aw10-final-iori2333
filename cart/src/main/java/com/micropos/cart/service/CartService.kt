package com.micropos.cart.service

import com.micropos.cart.models.Cart
import com.micropos.cart.models.PriceEntry
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

sealed interface CartService {
  fun getCart(cartId: String): Mono<Cart>
  fun newCart(): Mono<Cart>
  fun clearCart(cartId: String): Mono<Boolean>
  fun addToCart(cartId: String, productId: String, quantity: Int): Mono<Boolean>
  fun removeFromCart(cartId: String, productId: String, quantity: Int): Mono<Boolean>
  fun modifyCart(cartId: String, productId: String, quantity: Int): Mono<Boolean>
  fun countCart(cartId: String): Flux<PriceEntry>
}
