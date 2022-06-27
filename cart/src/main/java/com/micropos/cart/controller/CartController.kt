package com.micropos.cart.controller

import com.micropos.cart.mapper.CartMapper
import com.micropos.cart.service.CartService
import com.micropos.cart.api.CartApi
import com.micropos.cart.dto.CartDto
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

  override fun addProductToCart(productId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<CartDto>> {
    if (productId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .addToCart(productId, 1)
      .flatMap {
        if (it) {
          getCart(exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }

  override fun emptyCart(exchange: ServerWebExchange?): Mono<ResponseEntity<CartDto>> {
    return cartService
      .clearCart()
      .flatMap {
        if (it) {
          getCart(exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }

  override fun getCart(exchange: ServerWebExchange?): Mono<ResponseEntity<CartDto>> {
    return cartService
      .getCart()
      .map { cartMapper.toCartDto(it) }
      .map { ResponseEntity.ok(it) }
  }

  override fun removeProductFromCart(productId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<CartDto>> {
    if (productId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .removeFromCart(productId, 1)
      .flatMap {
        if (it) {
          getCart(exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }

  override fun updateProductQuantityInCart(
    productId: String?,
    quantity: Int?,
    exchange: ServerWebExchange?
  ): Mono<ResponseEntity<CartDto>> {
    if (productId == null || quantity == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return cartService
      .modifyCart(productId, quantity)
      .flatMap {
        if (it) {
          getCart(exchange)
        } else {
          Mono.just(ResponseEntity.notFound().build())
        }
      }
  }
}
