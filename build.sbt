name := "scala_spark_project"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.0.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.0.0",
  "org.apache.spark" % "spark-streaming_2.11" % "2.0.0",
  "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.0.0",
  "org.apache.spark" % "spark-streaming-flume_2.11" % "2.0.0",
  "org.apache.hadoop" % "hadoop-client" % "2.6.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.15"
)
