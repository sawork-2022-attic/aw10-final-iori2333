package com.micropos.cart.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class CounterConfig {
  @Bean
  fun webClient() = WebClient.create()
}
