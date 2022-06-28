package com.micropos.gateway.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

class ItemDto(
  @JsonProperty("productId") var productId: String,
  @JsonProperty("quantity") var quantity: BigDecimal
)
