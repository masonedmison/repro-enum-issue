val doobieVersion = "1.0.0-RC4"

lazy val root = Project("root", file("."))
  .settings(commonSettings)
  .settings(
    name := "Doobie",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.postgresql" % "postgresql" % "42.5.3",
    ),
  )

lazy val commonSettings = Seq(
  version := "0.1.0",
//  scalaVersion := "2.13.10",
  scalacOptions --= {
    if (sys.env.get("CI").isDefined) {
      Seq.empty
    } else {
      Seq("-Xfatal-warnings")
    }
  },
  scalacOptions ++= Seq("-Xsource:3"),
)
