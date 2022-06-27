package com.micropos.product.repository

import com.micropos.product.model.Product
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import springfox.documentation.annotations.Cacheable
import java.io.IOException
import java.net.URL

//@Repository
@Cacheable("products")
class JDRepository : ProductRepository {
  private var products: MutableCollection<Product> = mutableListOf()

  override fun allProducts(): List<Product> {
    try {
      parseJD("java")
    } catch (e: IOException) {
      products = mutableListOf()
    }

    return products.toList()
  }

  override fun findProductById(id: String): Product? {
    if (products.isEmpty()) {
      parseJD("java")
    }
    return products.find { it.id == id }
  }

  override fun page(page: Int, size: Int): List<Product> {
    return allProducts()
  }

  private fun parseJD(keyword: String) {
    if (products.isNotEmpty()) {
      return
    }
    val url = "https://search.jd.com/Search?keyword=$keyword&enc=utf-8"
    val document = Jsoup.parse(URL(url), 10000)
    val ul = document.getElementById("J_goodsList")
    val li = ul?.getElementsByTag("li")

    li?.forEach { el ->
      val id = el.attr("data-spu")
      val img = "https:" + el.getElementsByTag("img").eq(0).attr("data-lazy-img")
      val price = el.getElementsByAttribute("data-price").text()
      var title = el.getElementsByClass("p-name").eq(0).text()
      if (title.contains("，")) {
        title = title.substring(0, title.indexOf("，"))
      }
      val product = Product(id, title, price.toDouble(), img)
      products.add(product)
    }
  }
}
