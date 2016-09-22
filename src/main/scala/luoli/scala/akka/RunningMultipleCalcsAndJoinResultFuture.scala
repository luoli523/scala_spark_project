package luoli.scala.akka

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.util.Random

/**
  * Created by luoli on 16/9/19.
  */
object RunningMultipleCalcsAndJoinResultFuture extends App {

  println("Starting futures")
  val result1 = Cloud.algorithm(10)
  val result2 = Cloud.algorithm(20)
  val result3 = Cloud.algorithm(30)

  println("before for-comprehension")
  val result = for {
    r1 <- result1
    r2 <- result2
    r3 <- result3
  } yield (r1 + r2 + r3)

  println("before onSuccess")
  result onSuccess {
    case r=> println(s"total = ${r}") }
  println("before sleep at the end")
  sleep(2000) // keep the jvm alive

  def sleep(duration: Long) {
    Thread.sleep(duration)
  }
}

object Cloud {
  def algorithm(i:Int) : Future[Int] = Future {
    Thread.sleep(Random.nextInt(500))
    val result = i + 10
    println(s"algorithm from Cloud return: ${result}")
    result
  }
}
