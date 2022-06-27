package com.micropos.product.repository

import com.micropos.product.model.Product

sealed interface ProductRepository {
  fun allProducts(): List<Product>

  fun findProductById(id: String): Product?
}
