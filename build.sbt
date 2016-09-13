name := "AzureEventHubs-SparkExample"

version := "1.0"

scalaVersion := "2.10.0"

resolvers += "spark-eventhubs" at "https://raw.github.com/hdinsight/spark-eventhubs/maven-repo"


libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.6.2"
libraryDependencies += "com.microsoft.azure" % "spark-streaming-eventhubs_2.10" % "1.1.0"

dependencyOverrides +=  "org.scala-lang" % "scala-compiler" % "2.10.0"
