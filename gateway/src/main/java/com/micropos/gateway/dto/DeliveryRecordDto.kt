package com.micropos.gateway.dto

import com.fasterxml.jackson.annotation.JsonProperty

class DeliveryRecordDto(
  @JsonProperty("order") var order: OrderDto,
  @JsonProperty("status") var status: String,
  @JsonProperty("createdAt") var createdAt: Long
)
