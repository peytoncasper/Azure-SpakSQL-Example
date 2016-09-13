name := "AzureEventHubs-SparkExample"

version := "1.0"

scalaVersion := "2.10.6"

resolvers += "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven"


libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.6.2"
libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "1.6.2"
libraryDependencies += "org.apache.spark" % "spark-hive_2.10" % "1.6.2"
libraryDependencies += "org.apache.hadoop" % "hadoop-azure" % "2.7.0" excludeAll ExclusionRule(organization = "org.mortbay.jetty")
libraryDependencies += "com.microsoft.azure" % "azure-storage" % "2.0.0"
libraryDependencies += "datastax" % "spark-cassandra-connector" % "1.6.0-s_2.10"
