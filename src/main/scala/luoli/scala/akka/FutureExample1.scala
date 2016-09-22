package luoli.scala.akka

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by luoli on 16/9/19.
  */
object FutureExample1 extends App {

  val f = Future {
    sleep(500)
    1 + 1
  }

  // Here will BLOCK the thread !
  val result = Await.result(f, 1 second)
  print(result)

  def sleep(duration : Long): Unit = {
    Thread.sleep(duration)
  }
}
