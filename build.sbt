import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import xerial.sbt.Sonatype._

val scala213 = "2.13.8"

val scalaOptions =
  Seq(
    "-language:postfixOps",
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-Xfatal-warnings",
    "-Ywarn-dead-code",
    "-Ywarn-extra-implicit",
    "-Ywarn-unused:implicits",
    "-Ywarn-unused:imports",
    "-Ywarn-unused:locals",
    "-Ywarn-unused:params",
    "-Ywarn-unused:patvars",
    "-Ywarn-unused:privates",
    "-Xlint"
  )

val publishSettings = Seq[Setting[_]](
  crossScalaVersions := Seq(scala213),
  sonatypeProfileName := "com.github.simerplaha",
  publishMavenStyle := true,
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  publish := {},
  //  publishLocal := {},
  sonatypeProjectHosting := Some(GitHubHosting("simerplaha", "JustSQL", "simer.j@gmail.com")),
  developers := List(
    Developer(id = "simerplaha", name = "Simer JS Plaha", email = "simer.j@gmail.com", url = url("https://github.com/simerplaha/JustSQL"))
  ),
  scalacOptions ++= scalaOptions,
  publishTo := sonatypePublishTo.value,
  releaseCrossBuild := true,
  releaseVersionBump := sbtrelease.Version.Bump.Next,
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  releaseProcess :=
    Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publishSigned"),
      setNextVersion,
      commitNextVersion,
      releaseStepCommand("sonatypeReleaseAll"),
      pushChanges
    )
)

lazy val testDeps =
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.11" % Test,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5" % Test,
    "org.scalatest" %% "scalatest" % "3.2.12" % Test,
    "org.postgresql" % "postgresql" % "42.4.2"
  )

val commonSettings =
  Seq[Setting[_]](
    ThisBuild / scalaVersion := scala213,
    Global / concurrentRestrictions += Tags.limit(Tags.Test, 1)
  )

lazy val `JustSQL-root` =
  (project in file("."))
    .settings(
      commonSettings,
      publishSettings
    ).aggregate(justsql, `justsql-hikari`, `justsql-slick`)

lazy val justsql =
  project
    .settings(
      commonSettings,
      publishSettings,
      libraryDependencies ++= testDeps
    )

lazy val `justsql-hikari` =
  project
    .settings(
      commonSettings,
      publishSettings,
      libraryDependencies ++= testDeps :+ "com.zaxxer" % "HikariCP" % "5.0.1"
    ).dependsOn(justsql % "test->test;compile->compile")

lazy val `justsql-slick` =
  project
    .settings(
      commonSettings,
      publishSettings,
      libraryDependencies ++= testDeps :+ "com.typesafe.slick" %% "slick" % "3.3.3"
    ).dependsOn(justsql % "test->test;compile->compile")
