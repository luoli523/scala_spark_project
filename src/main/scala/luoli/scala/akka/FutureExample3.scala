package luoli.scala.akka

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.util.Random

/**
  * Created by luoli on 16/9/19.
  */
object FutureExample3 extends App {

  println("1 - starting calculation ...")
  val f = Future {
    sleep(Random.nextInt(300))
    42
  }

  println("2- before onComplete")
  f onComplete {
    case Success(value) => println(s"Got the callback, meaning = $value")
    case Failure(e) => e.printStackTrace()
  }

  // do the rest of your work
  println("A ..."); sleep(100)
  println("B ..."); sleep(100)
  println("C ..."); sleep(100)
  println("D ..."); sleep(100)
  println("E ..."); sleep(100)
  println("F ..."); sleep(100)

  def sleep(duration: Long) = {
    Thread.sleep(duration)
  }
}
