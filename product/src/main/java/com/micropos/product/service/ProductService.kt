package com.micropos.product.service

import com.micropos.product.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

sealed interface ProductService {
  fun getAllProducts(): Flux<Product>
  fun getProductById(id: String): Mono<Product?>
}
