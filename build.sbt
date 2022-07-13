ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "JustSQL",
    libraryDependencies ++=
      Seq(
        "com.typesafe.slick" %% "slick" % "3.3.3",
        "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
        "org.postgresql" % "postgresql" % "42.3.2",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
        "org.scalatest" %% "scalatest" % "3.2.10" % Test,
      )
  )
