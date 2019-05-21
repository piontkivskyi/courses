lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.6.0-M1"
lazy val catsVersion = "1.5.0-RC1"
lazy val slickVersion = "3.3.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.piontkivskyi",
      scalaVersion    := "2.12.7"
    )),
    name := "courses",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"   %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka"   %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka"   %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka"   %% "akka-stream"          % akkaVersion,
      "org.typelevel"       %% "cats-core"            % catsVersion,
      "com.typesafe.slick"  %% "slick"                % slickVersion,
      "org.slf4j"           % "slf4j-nop"             % "1.6.4",
      "com.typesafe.slick"  %% "slick-hikaricp"       % "3.3.0",
      "com.h2database"      % "h2"                    % "1.4.196",

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test
    )
  )