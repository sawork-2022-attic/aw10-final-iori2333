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

  override fun countCart(): Flux<PriceEntry> {
    logger.info("pos-cart: Count cart")
    return getCart()
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
