import sbt._

object Dependencies {
  val Scala212 = "2.12.20"

  val ZioVersion                    = "2.1.11"
  val ZioHttpVersion                = "3.0.1"
  val ZioJsonVersion                = "0.7.1"
  val ZioSchemaVersion              = "1.4.1"

  val `zio-http-gen`        = "dev.zio" %% "zio-http-gen"        % ZioHttpVersion
  val `zio-json-yaml`       = "dev.zio" %% "zio-json-yaml"       % ZioJsonVersion
  val `zio-schema`          = "dev.zio" %% "zio-schema"          % ZioSchemaVersion
  val `zio-schema-json`     = "dev.zio" %% "zio-schema-json"     % ZioSchemaVersion
  val `zio-test`            = "dev.zio" %% "zio-test"            % ZioVersion % Test
  val `zio-test-sbt`        = "dev.zio" %% "zio-test-sbt"        % ZioVersion % Test

}
