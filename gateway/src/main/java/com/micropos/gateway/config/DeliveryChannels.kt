package com.micropos.gateway.config

import com.micropos.gateway.dto.DeliveryRecordDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.webflux.dsl.WebFlux
import org.springframework.messaging.Message
import org.springframework.web.util.UriComponentsBuilder


@Configuration
class DeliveryChannels {
  private final val deliveryOrderId = "/api/delivery/{orderId}"

  private final val deliveryUrl = "http://localhost:8085/"

  @Bean
  fun inChannel(): IntegrationFlow? {
    return IntegrationFlows.from(WebFlux.inboundGateway(deliveryOrderId)
      .requestMapping { it.methods(HttpMethod.GET) }
      .payloadExpression("#pathVariables.orderId"))
      .headerFilter("accept-encoding", false)
      .channel("deliveryInChannel")
      .get()
  }

  @Bean
  fun outChannel(): IntegrationFlow? {
    return IntegrationFlows.from("deliveryInChannel")
      .handle(WebFlux.outboundGateway { it: Message<Any?> ->
        UriComponentsBuilder
          .fromUriString(deliveryUrl + deliveryOrderId)
          .buildAndExpand(it.payload)
          .toUri()
      }
        .httpMethod(HttpMethod.GET)
        .expectedResponseType(DeliveryRecordDto::class.java)
      )
      .get()
  }
}
