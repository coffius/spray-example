organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray"              %%  "spray-can"         % sprayV,
    "io.spray"              %%  "spray-routing"     % sprayV,
    "io.spray"              %%  "spray-testkit"     % sprayV  % "test",
    "com.typesafe.akka"     %%  "akka-actor"        % akkaV,
    "com.typesafe.akka"     %%  "akka-testkit"      % akkaV   % "test",
    "org.specs2"            %%  "specs2-core"       % "2.3.7" % "test",
    "com.typesafe.slick"    %%  "slick"             % "2.1.0",
    "joda-time"             %   "joda-time"         % "2.3",
    "org.postgresql"        %   "postgresql"        % "9.3-1101-jdbc41",
    "io.spray"              %%  "spray-json"        % "1.3.0",
    "org.joda"              %   "joda-convert"      % "1.6",
    "com.github.tototoshi"  %%  "slick-joda-mapper" % "1.2.0",
    "org.mockito"           %   "mockito-all"       % "1.9.5"
  )
}

fork := true

javaOptions := Seq("-Ddb.host=localhost", "-Ddb.port=5432", "-Ddb.name=spray-example")

Revolver.settings
