/**
  * Created by peyton on 9/10/16.
  */

//
import org.apache.spark.SparkConf
import org.apache.spark.streaming.eventhubs.EventHubsUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

object azure_event_hubs_spark_example {
  def createContext(checkpointDir: String, ehParams: Map[String, String], outputDir: String) : StreamingContext = {
    println("Creating new StreamingContext")

    // Set max number of cores to double the partition count
    val partitionCount = ehParams("eventhubs.partition.count").toInt
    val sparkConf = new SparkConf()
      .setAppName("EventCount")
      .set("spark.cores.max", (partitionCount*2).toString)
      .setMaster("spark://127.0.0.1:7077")

    // Set batch size
    val ssc =  new StreamingContext(sparkConf, Seconds(5))
//    ssc.checkpoint(checkpointDir)

    // Create a unioned stream for all partitions
    val stream = EventHubsUtils.createUnionStream(ssc, ehParams)

    // Create a single stream for one partition
    // val stream = EventHubsUtils.createStream(ssc, ehParams, "0")

    // Set checkpoint interval
//    stream.checkpoint(Seconds(10))

    // Count number of events in the past minute
    // val counts = stream.countByWindow(Minutes(1), Seconds(5))

    // Count number of events in the past batch
    val counts = stream.count()

//    counts.saveAsTextFiles(outputDir)
    counts.print()

    ssc
  }

  def main(args: Array[String]) {
    if (args.length < 8) {
      System.err.println(args.length)

      System.err.println("Usage: EventCount <checkpointDirectory> <policyname> <policykey>"
        + "<namespace> <name> <partitionCount> <consumerGroup> <outputDirectory>")
      System.exit(1)
    }
    val Array(checkpointDir, policy, key, namespace, name,
    partitionCount, consumerGroup, outputDir) = args
    val ehParams = Map[String, String](
      "eventhubs.policyname" -> policy,
      "eventhubs.policykey" -> key,
      "eventhubs.namespace" -> namespace,
      "eventhubs.name" -> name,
      "eventhubs.partition.count" -> partitionCount,
      "eventhubs.consumergroup" -> consumerGroup,
      "eventhubs.checkpoint.dir" -> checkpointDir,
      "eventhubs.checkpoint.interval" -> "10"
    )

    // Get StreamingContext from checkpoint directory, or create a new one
    val ssc = StreamingContext.getOrCreate(checkpointDir,
      () => {
        createContext(checkpointDir, ehParams, outputDir)
      })

    ssc.start()
    ssc.awaitTermination()
  }

}
