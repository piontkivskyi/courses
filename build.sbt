name := "courses"

version := "1.0"

scalaVersion := "2.12.1"

lazy val courses = (project in file("."))
  .settings(
    name := "courses",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )