package luoli.scala.akka

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by luoli on 16/9/18.
  */
class DrinkActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case "tea" => log.info("Tea time!")
    case "coffee" => log.info("Coffee time!")
    case _ => log.info("Hmmm....")
  }
}

import akka.actor.{Props, ActorSystem}

object ActorDemo {
  def main(args : Array[String]): Unit = {
    val system = ActorSystem("driver-actor-system")
    val props = Props[DrinkActor]
    val driverActor = system.actorOf(props, "drink-actor1")

    driverActor ! "tea"
    driverActor ! "coffee"
    driverActor ! "wator"

    //import scala.concurrent.duration._
    //system.awaitTermination(Duration(5, SECONDS))
    system.shutdown()

  }
}