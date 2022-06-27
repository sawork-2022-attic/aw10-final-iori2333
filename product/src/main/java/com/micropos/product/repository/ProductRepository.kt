package com.micropos.product.repository

import com.micropos.product.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

sealed interface ProductRepository {
  fun allProducts(): Flux<Product>

  fun findProductById(id: String): Mono<Product?>
}
