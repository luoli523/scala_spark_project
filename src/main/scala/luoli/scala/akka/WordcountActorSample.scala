package luoli.scala.akka

import akka.actor.{Actor, ActorRef, Props, ActorSystem}

case class ProcessStringMsg(string: String)
case class StringProcessedMsg(wordsNum: Int)

class StringCounterActor extends Actor {
  def receive = {
    case ProcessStringMsg(string) => {
      val wordsInLine = string.split(" ").length
      println(s"count $wordsInLine words in line")
      sender ! StringProcessedMsg(wordsInLine)
    }
    case _ => println("Error: message not recongnized")
  }
}

case class StartProcessFileMsg()

class WordCounterActor(fileName: String) extends Actor {
  private var running = false
  private var totalLines = 0
  private var linesProcessed = 0
  private var result = 0
  private var fileSender : Option[ActorRef] = None 

  def receive = {
    case StartProcessFileMsg() => {
      if (running) {
        println("Warning: duplicate start message received")
      } else {
        running = true
        fileSender = Some(sender)

        import scala.io.Source._
        fromFile(fileName).getLines.foreach {
          line =>
            context.actorOf(Props[StringCounterActor]) ! ProcessStringMsg(line)
            totalLines += 1
            println(s"processed $totalLines lines")
        }
      }
    }

    case StringProcessedMsg(wordsNum) => {
      result += wordsNum
      linesProcessed += 1
      if (linesProcessed == totalLines) {
        fileSender.map(_ ! result)
      }
    }

    case _ => println("message not recongnized")
  }
}

object WordCounterActorSample extends App {

  import akka.util.Timeout
  import scala.concurrent.duration._
  import akka.pattern.ask
  import akka.dispatch.ExecutionContexts._

  // import scala.concurrent.Implicites.global
  implicit val ec = global

  override def main(args: Array[String]) {
    val system = ActorSystem("WordCounterActorSystem")
    val actor = system.actorOf(Props(new WordCounterActor(args(0))))
    implicit val timeout = Timeout(25 seconds)
    val future = actor ? StartProcessFileMsg()
    future.map { result =>
      println("Total number of words: " + result)
      system.shutdown
    }
  }
}

















