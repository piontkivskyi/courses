name := "courses"

version := "1.0"

scalaVersion := "2.12.1"

lazy val courses = (project in file("."))
  .settings(
    name := "courses",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "org.typelevel" %% "cats-core" % "1.5.0-RC1"
    )
  )