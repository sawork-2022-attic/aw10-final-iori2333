package com.micropos.cart.service

import com.micropos.cart.dto.ProductDto
import com.micropos.cart.models.Cart
import com.micropos.cart.models.Item
import com.micropos.cart.models.PriceEntry
import com.micropos.cart.repository.CartRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CartServiceImpl : CartService {
  private final val logger = LoggerFactory.getLogger(javaClass)

  private final val productUrl = "http://localhost:8082/api/products/"

  @Autowired
  lateinit var cartRepository: CartRepository

  @Autowired
  lateinit var webClient: WebClient

  override fun getCart(cartId: String): Mono<Cart> {
    logger.info("pos-cart: Fetch cart info")
    return Mono.justOrEmpty(cartRepository.getCart(cartId))
  }

  override fun newCart(): Mono<Cart> {
    return Mono.fromCallable { cartRepository.createCart() }
  }

  override fun clearCart(cartId: String): Mono<Boolean> {
    logger.info("pos-cart: Clear cart")
    return getCart(cartId)
      .map {
        it.items.clear()
        true
      }
      .defaultIfEmpty(false)
  }

  override fun addToCart(cartId: String, productId: String, quantity: Int): Mono<Boolean> {
    logger.info("pos-cart: Add product $productId ($quantity) to cart")
    return getCart(cartId)
      .map {
        it.addItem(Item(productId, quantity))
        true
      }
      .defaultIfEmpty(false)
  }

  override fun removeFromCart(cartId: String, productId: String, quantity: Int): Mono<Boolean> {
    logger.info("pos-cart: Remove product $productId ($quantity) from cart")
    return getCart(cartId)
      .map {
        it.removeItem(Item(productId, quantity))
        true
      }
      .defaultIfEmpty(false)
  }

  override fun modifyCart(cartId: String, productId: String, quantity: Int): Mono<Boolean> {
    logger.info("pos-cart: Modify product $productId ($quantity) in cart")
    return getCart(cartId)
      .map {
        it.modifyItem(Item(productId, quantity))
        true
      }
      .defaultIfEmpty(false)
  }

  override fun countCart(cartId: String): Flux<PriceEntry> {
    logger.info("pos-cart: Count cart")
    return getCart(cartId)
      .map { it.items }
      .flatMapIterable { it }
      .flatMap { item ->
        webClient.get()
          .uri(productUrl + item.productId)
          .retrieve()
          .toEntity(ProductDto::class.java)
          .map { res ->
            val price = if (res.statusCode == HttpStatus.OK) {
              res.body!!.price.toDouble() * item.quantity
            } else {
              0.0
            }
            PriceEntry(item.productId, item.quantity, price)
          }
      }
  }
}
