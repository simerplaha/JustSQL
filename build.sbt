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

lazy val root =
  (project in file("."))
    .settings(
      name := "JustSQL-root",
      ThisBuild / scalaVersion := scala213,
      publishSettings
    ).aggregate(JustSQL, HikariDS, SlickDS)

lazy val JustSQL =
  (project in file("JustSQL"))
    .settings(
      name := "JustSQL",
      ThisBuild / scalaVersion := scala213,
      publishSettings,
      libraryDependencies ++=
        Seq(
          /** TEST */
          "ch.qos.logback" % "logback-classic" % "1.2.11" % Test,
          "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5" % Test,
          "org.scalatest" %% "scalatest" % "3.2.12" % Test,
          "org.postgresql" % "postgresql" % "42.4.2" % Test
        )
    ).dependsOn(HikariDS % Test)

lazy val HikariDS =
  (project in file("HikariDS"))
    .settings(
      name := "JustSQL-hikariDS",
      ThisBuild / scalaVersion := scala213,
      publishSettings,
      libraryDependencies += "com.zaxxer" % "HikariCP" % "5.0.1"
    )

lazy val SlickDS =
  (project in file("SlickDS"))
    .settings(
      name := "JustSQL-SlickDS",
      ThisBuild / scalaVersion := scala213,
      publishSettings,
      libraryDependencies += "com.typesafe.slick" %% "slick" % "3.3.3"
    ).dependsOn(JustSQL)

