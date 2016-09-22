package luoli.scala.akka

import akka.actor.{Actor, Props}
import akka.event.Logging
import luoli.scala.akka.DeveloperActor.Feature

class DeveloperActor extends Actor {
  val log = Logging(context.system, this)

  import DeveloperActor._
  def receive = {
    case feature: Feature => log.info(s"Working on a feature: ${feature.name}")
    case Bug => log.info(s"Fixing a bug")
    case _ => log.info("Play StarCraft2")
  }
}

object DeveloperActor {
  def props = Props(new DeveloperActor)

  case class Feature(name:String)
  object Bug
}

class TesterActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case feature: Feature => log.info(s"Testing ${feature.name}")
    case _ => log.info("Watching youtube")
  }
}

object TesterActor {
  def props = Props(new TesterActor)
}

/**
  * Created by luoli on 16/9/18.
  */

import akka.actor.ActorSystem
import luoli.scala.akka.DeveloperActor._

object ActorMessageDemo {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("driver-system")
    val developer = system.actorOf(DeveloperActor.props, "developer-luoli")
    val tester = system.actorOf(TesterActor.props, "QA-fanti")

    developer ! Feature("niubi bug")
    tester.tell(Feature("TheNewFeature"), developer)

    system.shutdown()
  }
}
