import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SimpleApp {
	def main(args: Array[String]) {
		val logFile = "/Users/luoli/dev/git/luoli/spark/README.md"
		val conf = new SparkConf().setAppName("SimpleApp")
		val sc = new SparkContext(conf)

		val logData = sc.textFile(logFile, 2).cache()
		val numAs = logData.filter(line => line.contains("a")).count()
		val numBs = logData.filter(line => line.contains("b")).count()

		print("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
	}
}