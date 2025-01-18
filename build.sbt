import Dependencies._

lazy val zioHttpGenSbt = (project in file("zio-http-gen-sbt-plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    organization           := "dev.zio",   
    name                   := "zio-http-sbt-codegen",
    version                := "0.1.0", // TODO: after first git tag, remove this, and infer version from tag.
    sbtPlugin              := true,
    scalaVersion           := Scala212,
    licenses               := Seq("Apache-2.0" -> url("https://github.com/zio/zio-http-sbt/blob/main/LICENSE")),
    sonatypeCredentialHost := "oss.sonatype.org",
    sonatypeRepository     := "https://oss.sonatype.org/service/local",
    sonatypeProfileName    := "dev.zio",
    publishTo              := sonatypePublishToBundle.value,
    sonatypeTimeoutMillis  := 300 * 60 * 1000,
    publishMavenStyle      := true,
    credentials           ++= (for {
        username <- Option(System.getenv().get("SONATYPE_USERNAME"))
        password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
      } yield Credentials(
        "Sonatype Nexus Repository Manager",
        "oss.sonatype.org",
        username,
        password,
      )).toSeq,
    sbtTestDirectory       := sourceDirectory.value / "sbt-test",
    scriptedLaunchOpts     += ("-Dplugin.version=" + version.value),
    scriptedBufferLog      := false,
    libraryDependencies   ++= Seq(
      `zio-http-gen`,
      `zio-json-yaml`,
      `zio-schema`,
      `zio-schema-json`,
      `zio-test`,
      `zio-test-sbt`
    ),

  )
