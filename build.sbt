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
    "-Xlint"
  )

val publishSettings = Seq[Setting[_]](
  crossScalaVersions := Seq(scala213),
  sonatypeProfileName := "com.github.simerplaha",
  publishMavenStyle := true,
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  publish := {},
  publishLocal := {},
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
    "org.postgresql" % "postgresql" % "42.4.2" % Test
  )

lazy val root =
  (project in file("."))
    .settings(
      name := "JustSQL-root",
      ThisBuild / scalaVersion := scala213,
      publishSettings
    ).aggregate(JustSQL, `interop-hikari`, `interop-slick`)

lazy val JustSQL =
  project
    .settings(
      name := "JustSQL",
      ThisBuild / scalaVersion := scala213,
      publishSettings,
      libraryDependencies ++= testDeps
    )

lazy val `interop-hikari` =
  project
    .settings(
      ThisBuild / scalaVersion := scala213,
      publishSettings,
      libraryDependencies ++= testDeps :+ "com.zaxxer" % "HikariCP" % "5.0.1"
    ).dependsOn(JustSQL % "test->test;compile->compile")

lazy val `interop-slick` =
  project
    .settings(
      ThisBuild / scalaVersion := scala213,
      publishSettings,
      libraryDependencies ++= testDeps :+ "com.typesafe.slick" %% "slick" % "3.3.3"
    ).dependsOn(JustSQL % "test->test;compile->compile")
