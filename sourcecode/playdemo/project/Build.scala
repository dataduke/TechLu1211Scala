import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "playdemo"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "com.typesafe.akka" % "akka-remote" % "2.0.2"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here
      // resolvers += MavenRepository
    )

}
