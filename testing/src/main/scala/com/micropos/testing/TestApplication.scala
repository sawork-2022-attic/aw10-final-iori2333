package com.micropos.testing

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object TestApplication {
  def main(args: Array[String]): Unit = {
    val runningPath = System.getProperty("user.dir")
    val props = new GatlingPropertiesBuilder()
      .resultsDirectory(runningPath)
      .simulationClass("com.micropos.testing.PosSimulation")
      .build
    Gatling.fromMap(props)
  }
}
