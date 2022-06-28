package com.micropos.cart.controller

import com.micropos.cart.mapper.CartMapper
import com.micropos.cart.service.CartService
import com.micropos.cart.api.CartApi
import com.micropos.cart.dto.CartDto
import com.micropos.cart.dto.CounterDto
import com.micropos.cart.models.Counter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class CartController : CartApi {
  @Autowired
  private lateinit var cartService: CartService

  @Autowired
  private lateinit var cartMapper: CartMapper

  override fun createCart(exchange: ServerWebExchange?): Mono<ResponseEntity<CartDto>> {
    return cartService.newCart()
      .map { cart -> cartMapper.toCartDto(cart) }
      .map { cart -> ResponseEntity.ok(cart) }
  }

  override fun getCart(cartId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<CartDto>> {
    if (cartId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .getCart(cartId)
      .map { cartMapper.toCartDto(it) }
      .map { ResponseEntity.ok(it) }
  }

  override fun addProductToCart(
    cartId: String?,
    productId: String?,
    exchange: ServerWebExchange?
  ): Mono<ResponseEntity<CartDto>> {
    if (productId == null || cartId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .addToCart(cartId, productId, 1)
      .flatMap {
        if (it) {
          getCart(cartId, exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }

  override fun emptyCart(cartId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<CartDto>> {
    if (cartId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .clearCart(cartId)
      .flatMap {
        if (it) {
          getCart(cartId, exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }

  override fun removeProductFromCart(
    cartId: String?,
    productId: String?,
    exchange: ServerWebExchange?
  ): Mono<ResponseEntity<CartDto>> {
    if (productId == null || cartId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .removeFromCart(cartId, productId, 1)
      .flatMap {
        if (it) {
          getCart(cartId, exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }

  override fun updateProductQuantityInCart(
    cartId: String?,
    productId: String?,
    quantity: Int?,
    exchange: ServerWebExchange?
  ): Mono<ResponseEntity<CartDto>> {
    if (cartId == null || productId == null || quantity == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .modifyCart(cartId, productId, quantity)
      .flatMap {
        if (it) {
          getCart(cartId, exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }

  override fun checkoutCart(cartId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<CounterDto>> {
    if (cartId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .countCart(cartId)
      .collectList()
      .map { Counter(it, it.sumOf { e -> e.price }) }
      .map { ResponseEntity.ok(cartMapper.toCounterDto(it)) }
  }
}
