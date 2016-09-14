/**
  * Created by peyton on 9/10/16.
  */

//
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.hive._
import org.apache.spark.{SparkConf, SparkContext}

object azure_spark_sql_test {

  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("SparkSQLTest")
      .set("spark.cassandra.connection.host", "127.0.0.1")
      .setMaster("spark://127.0.0.1:7077")
//      .setMaster("local[1]")
    val sc = new SparkContext(sparkConf)
    val hiveContext = new HiveContext(sc)
    val hadoopConf=sc.hadoopConfiguration;
    hadoopConf.set("fs.azure.account.key.account_name.blob.core.windows.net", "key")

    val base_address = "wasb://container_name@account_name.blob.core.windows.net/"

    val data = sc.textFile(base_address + "2016/08/29/00/1596422213_8d4bfbdeb0364be6913a80292a4ef606_1.csv")
    val seq_data = data.map(x => {
      val data = x.split(',')
      (data(0), data(1), data(2), data(2), data(3), data(4), data(5), data(6), data(7))
    })

    seq_data.foreach(string => println(string))

    // Create an RDD From Raw Blob Storage data
    val hiveRDD = hiveContext.createDataFrame(seq_data)

    //Writing a new Spark SQL Created Hive Table
    hiveRDD.write.orc(base_address + "test_hive_table")

    // Reading from Spark SQL Created Hive Table
    val createdTable = hiveContext.read.orc(base_address + "test_hive_table")

    // Performing a SELECT statement on Hive Table
    createdTable.select("_1")

    // Inserting new data
    hiveRDD.write.mode(SaveMode.Overwrite).orc(base_address + "test_hive_table")

//    seq_data.saveToCassandra("danieltest", "events", SomeColumns("type", "user", "usertype", "success", "rk", "eventprocessedutctime", "partitionid", "eventenqueuedutctime"))



  }

}
