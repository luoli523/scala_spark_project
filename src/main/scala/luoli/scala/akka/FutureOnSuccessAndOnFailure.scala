package luoli.scala.akka

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

/**
  * Created by luoli on 16/9/19.
  */
object FutureOnSuccessAndOnFailure extends App {

  val f = Future {
    sleep(Random.nextInt(300))
    if (Random.nextInt(500) > 250) throw new Exception("Failed!") else 42
  }

  f onSuccess {
    case x => println(s"Success: ${x}")
  }

  f onFailure {
    case e => println(s"Exception: ${e.getMessage}")
  }

  def longRunningComputation(i : Int) : Future[Int] = Future {
    sleep(500)
    if (Random.nextInt(500) > 250) throw new Exception("Failed!") else i + 1
  }

  longRunningComputation(3).onComplete {
    case Success(value) => println(s"longRunningComputation result = ${value}")
    case Failure(e) => e.printStackTrace()
  }

  // do the rest of your work
  println("A ..."); sleep(100)
  println("B ..."); sleep(100)
  println("C ..."); sleep(100)
  println("D ..."); sleep(100)
  println("E ..."); sleep(100)
  println("F ..."); sleep(100)

  sleep(1000)

  def sleep(duration: Long) = {
    Thread.sleep(duration)
  }
}
