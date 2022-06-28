package com.micropos.testing

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class PosSimulation extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn: ScenarioBuilder = scenario("MicroPos Reactive")
    .exec(http("request").get("/api/products"))
    .exec(http("request").get("/api/cart"))
    .exec(
      http("request")
        .post("/api/order")
        .body(StringBody("""[{"productId":"0077595696","quantity":4}]"""))
        .asJson
    )
    .exec(
      http("request")
        .post("/api/order")
        .body(StringBody("""[{"productId":"0077595696","quantity":12}]"""))
        .asJson
    )
    .exec(http("request").get("/api/order"))

  setUp(scn.inject(atOnceUsers(500)).protocols(httpProtocol))
}
