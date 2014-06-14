name := "spark-jobserver-examples"

version := "1.0.0"

scalacOptions ++= Seq("-deprecation")

lazy val buildSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "com.github.fedragon.sparking.jobserver",
  scalaVersion := "2.10.3"
)

resolvers += "Ooyala Bintray" at "http://dl.bintray.com/ooyala/maven"

libraryDependencies ++= Seq (
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.2",
  ("org.apache.spark" %% "spark-core" % "0.9.0-incubating").
    exclude("org.mortbay.jetty", "servlet-api").
    exclude("commons-beanutils", "commons-beanutils-core").
    exclude("commons-collections", "commons-collections").
    exclude("com.esotericsoftware.minlog", "minlog").
    exclude("junit", "junit").
    exclude("org.slf4j", "log4j12"),
  "ooyala.cnd" % "job-server" % "0.3.1" % "provided"
)
