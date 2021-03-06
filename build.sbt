name := """porrify"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
	jdbc,
	anorm,
	cache,
	ws,
	"com.typesafe.slick" %% "slick" % "2.1.0",
	"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)