name := "WiseCrack"

version := "1.0"


scalaVersion := "2.11.7"

val akkaVersion = "2.4.0"
val sprayVersion = "1.3.3"
val jacksonVersion = "2.6.3"
val logbackVersion = "1.1.3"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.play"               %  "play-json_2.11"            % "2.4.3",
  "com.typesafe.akka"               %% "akka-actor"                 % akkaVersion,
  "com.typesafe.akka"               %% "akka-contrib"               % akkaVersion,
  "com.typesafe.akka"               %% "akka-slf4j"                 % akkaVersion,
  "io.spray"                        %% "spray-can"                  % sprayVersion,
  "io.spray"                        %% "spray-routing-shapeless2"   % sprayVersion, //exclude("com.chuusai", "shapeless_2.11"),
  "com.github.kikuomax"             %%  "spray-jwt-shapeless2"     % "0.0.1",
  "io.spray"                        %% "spray-json"                 % "1.3.2",
  "org.json4s"                      %% "json4s-native"              % "3.2.11",
  "com.fasterxml.jackson.datatype"  %  "jackson-datatype-joda"      % jacksonVersion,
  "com.fasterxml.jackson.module"    %% "jackson-module-scala"       % jacksonVersion,
  "io.github.cloudify"              %% "scalazon"                   % "0.11",
  "org.joda"                        %  "joda-convert"               % "1.2",
  "com.typesafe.scala-logging"      %% "scala-logging-slf4j"        % "2.1.2",
  "ch.qos.logback"                  %  "logback-classic"            % logbackVersion,
  "mysql" % "mysql-connector-java" % "5.1.12"
)
