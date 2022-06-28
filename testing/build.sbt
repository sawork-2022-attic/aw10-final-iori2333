ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

mainClass := Some("com.micropos.testing.TestApplication")

lazy val root = (project in file("."))
  .settings(
    name := "testing"
  )

enablePlugins(GatlingPlugin)

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.7.6"
libraryDependencies += "io.gatling" % "gatling-test-framework" % "3.7.6"
