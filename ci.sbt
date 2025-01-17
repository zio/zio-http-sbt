import Dependencies.*

import scala.concurrent.duration.DurationInt

// CI Configuration
ThisBuild / githubWorkflowEnv += ("JDK_JAVA_OPTIONS" -> "-Xms4G -Xmx8G -XX:+UseG1GC -Xss10M -XX:ReservedCodeCacheSize=1G -XX:NonProfiledCodeHeapSize=512m -Dfile.encoding=UTF-8")
ThisBuild / githubWorkflowEnv += ("SBT_OPTS" -> "-Xms4G -Xmx8G -XX:+UseG1GC -Xss10M -XX:ReservedCodeCacheSize=1G -XX:NonProfiledCodeHeapSize=512m -Dfile.encoding=UTF-8")

ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("snapshots")

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.graalvm(Graalvm.Distribution("graalvm"), "17"),
  JavaSpec.graalvm(Graalvm.Distribution("graalvm"), "21"),
  JavaSpec.temurin("17"),
  JavaSpec.temurin("21"),
)
ThisBuild / githubWorkflowPREventTypes := Seq(
  PREventType.Opened,
  PREventType.Synchronize,
  PREventType.Reopened,
  PREventType.Edited,
  PREventType.Labeled,
)

val coursierSetup =
  WorkflowStep.Use(
    UseRef.Public("coursier", "setup-action", "v1"),
    params = Map("apps" -> "sbt"),
  )

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowDependencyPatterns ++= Seq("project/Dependencies.scala")
ThisBuild / githubWorkflowPublishTargetBranches += RefPredicate.StartsWith(Ref.Tag("v"))
ThisBuild / githubWorkflowPublish       :=
  Seq(
    WorkflowStep.Sbt(
      List("ci-release"),
      name = Some("Release"),
      env = Map(
        "PGP_PASSPHRASE"      -> "${{ secrets.PGP_PASSPHRASE }}",
        "PGP_SECRET"          -> "${{ secrets.PGP_SECRET }}",
        "SONATYPE_PASSWORD"   -> "${{ secrets.SONATYPE_PASSWORD }}",
        "SONATYPE_USERNAME"   -> "${{ secrets.SONATYPE_USERNAME }}",
        "CI_SONATYPE_RELEASE" -> "${{ secrets.CI_SONATYPE_RELEASE }}",
      ),
    )
  )

ThisBuild / githubWorkflowBuild := {
  (ThisBuild / githubWorkflowBuild).value ++ WorkflowJob(
    "testSbtPlugin",
    "Test sbt plugin",
    List(
      WorkflowStep.Use(UseRef.Public("coursier", "setup-action", "v1")),
      WorkflowStep.Run(
        name = Some(s"Test sbt plugin"),
        commands = List(s"sbt zioHttpGenSbt/scripted"),
        cond = Some(s"$${{ github.event_name == 'pull_request' }}"),
      ),
    ),
    scalas = List(Scala212),
  ).steps
}

ThisBuild / githubWorkflowBuildTimeout := Some(60.minutes)