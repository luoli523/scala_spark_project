package luoli.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.HashPartitioner
import org.apache.spark.streaming._

/**
 * Counts words cumulatively in UTF8 encoded, '\n' delimited text received from the network every
 * second starting with initial value of word count.
 * Usage: SparkStatefulNetworkWordCount <hostname> <port>
 *   <hostname> and <port> describe the TCP server that Spark Streaming would connect to receive
 *   data.
 *
 * To run this on your local machine, you need to first run a Netcat server
 *    `$ nc -lk 9999`
 * and then run the example
 *    `$ bin/run-example
 *      org.apache.spark.examples.streaming.SparkStatefulNetworkWordCount localhost 9999`
 */
object SparkStatefulNetworkWordCount {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: SparkStatefulNetworkWordCount <hostname> <port>")
      System.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("SparkStatefulNetworkWordCount")
    // Create the context with a 1 second batch size
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc.checkpoint(".")

    // Initial state RDD for mapWithState operation
    val initialRDD = ssc.sparkContext.parallelize(List(("hello", 1), ("world", 1)))

    // Create a ReceiverInputDStream on target ip:port and count the
    // words in input stream of \n delimited test (eg. generated by 'nc')
    val lines = ssc.socketTextStream(args(0), args(1).toInt)
    val words = lines.flatMap(_.split(" "))
    val wordDstream = words.map(x => (x, 1))

    // Update the cumulative count using mapWithState
    // This will give a DStream made of state (which is the cumulative count of the words)
    val mappingFunc = (word: String, one: Option[Int], state: State[Int]) => {
      val sum = one.getOrElse(0) + state.getOption.getOrElse(0)
      val output = (word, sum)
      state.update(sum)
      output
    }

    val stateDstream = wordDstream.mapWithState(
      StateSpec.function(mappingFunc).initialState(initialRDD))
    stateDstream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
// scalastyle:on println
