ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "log-parser"
  )

libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.10" % "test"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"
