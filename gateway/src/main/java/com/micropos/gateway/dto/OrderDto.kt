package com.micropos.gateway.dto

import com.fasterxml.jackson.annotation.JsonProperty

class OrderDto(
  @JsonProperty("id") var id: String,
  @JsonProperty("items") var items: MutableList<ItemDto>,
  @JsonProperty("status") var status: String
)
