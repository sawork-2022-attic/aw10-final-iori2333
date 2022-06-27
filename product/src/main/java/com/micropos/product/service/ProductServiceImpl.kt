package com.micropos.product.service

import com.micropos.product.model.Product
import com.micropos.product.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductServiceImpl : ProductService {
  private final val logger = LoggerFactory.getLogger(javaClass)

  @Autowired
  private lateinit var productRepository: ProductRepository

  override fun getAllProducts(page: Int, size: Int): Flux<Product> {
    logger.info("pos-product: Fetching all products")
    return Flux.fromIterable(productRepository.page(page, size))
  }

  override fun getProductById(id: String): Mono<Product> {
    logger.info("pos-product: Fetching product with id: $id")
    return Mono.fromCallable { productRepository.findProductById(id) }
  }
}
