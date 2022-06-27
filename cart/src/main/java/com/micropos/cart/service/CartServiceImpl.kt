package com.micropos.cart.service

import com.micropos.cart.models.Cart
import com.micropos.cart.models.Item
import com.micropos.cart.repository.CartRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CartServiceImpl : CartService {
  private final val logger = LoggerFactory.getLogger(javaClass)

  @Autowired
  lateinit var cartRepository: CartRepository

  override fun getCart(): Mono<Cart> {
    logger.info("pos-cart: Fetch cart info")
    return Mono.fromCallable { cartRepository.getCart() }
  }

  override fun clearCart(): Mono<Boolean> {
    logger.info("pos-cart: Clear cart")
    return getCart().map {
      it.items.clear()
      true
    }
  }

  override fun addToCart(product: String, quantity: Int): Mono<Boolean> {
    logger.info("pos-cart: Add product $product ($quantity) to cart")
    return getCart().map {
      it.addItem(Item(product, quantity))
      true
    }

  }

  override fun removeFromCart(product: String, quantity: Int): Mono<Boolean> {
    logger.info("pos-cart: Remove product $product ($quantity) from cart")
    return getCart().map {
      it.removeItem(Item(product, quantity))
      true
    }

  }

  override fun modifyCart(product: String, quantity: Int): Mono<Boolean> {
    logger.info("pos-cart: Modify product $product ($quantity) in cart")
    return getCart().map {
      it.modifyItem(Item(product, quantity))
      true
    }
  }
}
