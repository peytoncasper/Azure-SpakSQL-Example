/**
  * Created by peyton on 9/10/16.
  */

//
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.{SparkConf, SparkContext}

object azure_spark_sql_test {

  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("SparkSQLTest")
      .set("spark.cassandra.connection.host", "127.0.0.1")
      .setMaster("spark://127.0.0.1:7077")
      .setMaster("local[1]")

      .set("", "")
    val sc = new SparkContext(sparkConf)
    val hadoopConf=sc.hadoopConfiguration;
    hadoopConf.set("fs.azure.account.key.ACCOUNT_NAME_OF_STORAGE_ACCOUNT.blob.core.windows.net", "storage_account_key")

    val data = sc.textFile("wasb://CONTAINER_NAME@ACCOUNT_NAME_OF_STORAGE_ACCOUNT.blob.core.windows.net/2016/08/29/00/1596422213_8d4bfbdeb0364be6913a80292a4ef606_1.csv")
    val seq_data = data.map(x => {
      val data = x.split(',')
      (data(0), data(1), data(2), data(2), data(3), data(4), data(5), data(6), data(7))
    })

    seq_data.foreach(string => println(string))

    seq_data.saveToCassandra("danieltest", "events", SomeColumns("type", "user", "usertype", "success", "rk", "eventprocessedutctime", "partitionid", "eventenqueuedutctime"))



  }

}
