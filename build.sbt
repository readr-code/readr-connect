organization := "com.readr"

name := "connect"

version := "1.0-SNAPSHOT"

resourceDirectory in Compile <<= baseDirectory { _ / "conf" }

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.1",
  "com.readr" %% "model" % "1.1-SNAPSHOT",
  "com.readr" %% "client" % "1.1-SNAPSHOT",
  "org.json4s" %% "json4s-jackson" % "3.2.9"
)     

resolvers ++= Seq(
  "Readr snapshots" at "http://snapshots.mvn-repo.readr.com",
  "Readr releases" at "http://releases.mvn-repo.readr.com"
)

resolvers += "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

