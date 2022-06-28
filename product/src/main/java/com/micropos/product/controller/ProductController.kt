package com.micropos.product.controller

import com.micropos.product.api.ProductsApi
import com.micropos.product.dto.ProductDto
import com.micropos.product.mapper.ProductMapper
import com.micropos.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.springframework.cache.annotation.Cacheable

@Controller
@RequestMapping("/api")
class ProductController : ProductsApi {
  @Autowired
  private lateinit var productService: ProductService

  @Autowired
  private lateinit var productMapper: ProductMapper

  @Cacheable(value = ["products"])
  override fun listProducts(
    page: Int?,
    size: Int?,
    exchange: ServerWebExchange?
  ): Mono<ResponseEntity<Flux<ProductDto>>> {
    if (page == null || size == null || size > 255 || page <= 0 || size <= 0) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return Mono.fromCallable {
      ResponseEntity.ok(productService.getAllProducts(page, size).map { productMapper.toProductDto(it) })
    }
  }

  @Cacheable(value = ["products"], key = "#productId")
  override fun showProductById(productId: String?, exchange: ServerWebExchange?): Mono<ResponseEntity<ProductDto>> {
    if (productId == null) {
      return Mono.just(ResponseEntity.badRequest().build())
    }
    return productService
      .getProductById(productId)
      .map { ResponseEntity.ok(productMapper.toProductDto(it)) }
      .defaultIfEmpty(ResponseEntity.notFound().build())
  }
}
