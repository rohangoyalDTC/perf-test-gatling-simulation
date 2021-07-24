import sbt.io.Path.contentOf

name := """perf-test-simulation"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .disablePlugins(PlayLayoutPlugin)
  .enablePlugins(GatlingPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    Universal / mappings ++= contentOf(sourceDirectory.value / "main" / "resources")
      .map { case (resource, path) => resource -> ("conf/" + path) },
    Universal / javaOptions ++= Seq(
      "-Dgatling.conf.file=conf/gatling.conf",
    )

  )

scalaVersion := "2.13.6"
val gatlingVersion = "3.5.1"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion,
  "io.gatling" % "gatling-test-framework" % gatlingVersion,
)

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.11.4",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.11.4",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.4",
)
