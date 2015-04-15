name := "reactive-streams-interop"

organization := "com.rolandkuhn"

version := "0.1.0-SNAPSHOT"

resolvers += "jfrog" at "http://oss.jfrog.org/repo"

resolvers += "spring" at "http://repo.spring.io/libs-milestone"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream-experimental" % "0.10-M1",
    "io.reactivex" % "rxjava-reactive-streams" % "0.5.0",
    "io.ratpack" % "ratpack-test" % "0.9.16-SNAPSHOT",
    "org.projectreactor" % "reactor-core" % "2.0.0.M1",
    "org.slf4j" % "slf4j-simple" % "1.7.12"
  )
