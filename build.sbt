ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "JustSQL",
    libraryDependencies ++=
      Seq(
        /** LOGS */
        "ch.qos.logback" % "logback-classic" % "1.2.11" % Test,
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5" % Test,
        /** DATABASE */
        "com.zaxxer" % "HikariCP" % "5.0.1" % Test,
        "org.postgresql" % "postgresql" % "42.3.6" % Test,
        /** TEST */
        "org.scalatest" %% "scalatest" % "3.2.12" % Test,
      )
  )
