package com.micropos.product.repository

import com.micropos.product.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
@Cacheable("products")
class AmazonRepository : ProductRepository {
  @Autowired
  lateinit var mongoTemplate: MongoTemplate

  private fun com.micropos.product.model.mongo.Product.toProduct() = Product(
    asin, title, price,
    imageURLHighRes.firstOrNull()
  )

  override fun allProducts(): List<Product> {
    return mongoTemplate
      .findAll(com.micropos.product.model.mongo.Product::class.java)
      .map { it.toProduct() }
  }

  override fun findProductById(id: String): Product? {
    return mongoTemplate
      .findById(id, com.micropos.product.model.mongo.Product::class.java)
      ?.toProduct()
  }

  override fun page(page: Int, size: Int): List<Product> {
    val query = Query()
      .skip(page.toLong() * size)
      .limit(size)
    return mongoTemplate
      .find(query, com.micropos.product.model.mongo.Product::class.java)
      .map { it.toProduct() }
  }
}
