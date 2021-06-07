name := "tic-tac-toe"
organization := "com.carvana"
version := "0.1"
scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.2" % "test",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
