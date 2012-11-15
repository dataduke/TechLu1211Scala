import sbt._
import sbt.Keys._

object AkkaCalculatorBuild extends Build {

  lazy val akkaCalculator = Project(
    id = "akka-calculator",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Akka Calculator",
      organization := "org.example",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.1",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies ++= Seq(
		"com.typesafe.akka" % "akka-kernel" % "2.0.2",
		"com.typesafe.akka" % "akka-actor" % "2.0.2",
		"com.typesafe.akka" % "akka-remote" % "2.0.2"
	  )
    )
  )
}
