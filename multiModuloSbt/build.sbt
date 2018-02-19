name := "SBT Sub Projects"

//version := "1.0"


lazy val commonSettings = Seq(
	organization := "es.example",
	version := "0.1.0-SNAPSHOT",
	scalaVersion := "2.11.8"
)

lazy val foo = (project in file("Foo"))


lazy val bar = (project in file("Bar"))



lazy val root = (project in file("."))
	.settings(commonSettings)
	.aggregate(foo, bar)	// clean
	.dependsOn(foo, bar)	// depencias
