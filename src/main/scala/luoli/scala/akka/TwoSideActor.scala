package luoli.scala.akka

import scala.util.Random
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import luoli.scala.akka.Developer.{Bug, Feature, Fix}

class Tester extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case feature : Feature => {
      log.info(s"Testing '${feature.name}'...")
      val bugExists = Random.nextInt(10)
      if (bugExists < 7) {
        log.info("A bug was found")
        sender() ! Bug
      }
    }
    case Fix => log.info(s"Verifying the fix...")
    case _ => log.info("Watching YouTube")
  }
}

object Tester {
  def props = Props(new Tester)
}

class Developer(tester: ActorRef) extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case feature: Feature => {
      log.info(s"Working on feature '${feature.name}'")
      tester ! feature
    }
    case Bug => {
      log.info("Fixing a bug")
      tester ! Fix
    }
    case _ => log.info("Play StarCraft2")
  }

}

object Developer {
  def props(tester: ActorRef) = Props(new Developer(tester))

  case class Feature(name: String)
  object Bug
  object Fix
}

/**
  * Created by luoli on 16/9/18.
  */
object TwoSideActor {
  def main(args: Array[String]) = {
    val system = ActorSystem("MyActorSystem")
    val tester = system.actorOf(Tester.props, "tester")
    val developer = system.actorOf(Developer.props(tester), "developer")

    developer ! Feature("Social integration")

    system.shutdown()
  }
}
