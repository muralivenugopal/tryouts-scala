package my.sample.akka.deathwatch.cloud

import java.io.FileNotFoundException
import scala.concurrent.duration.Duration
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.OneForOneStrategy
import akka.actor.PoisonPill
import akka.actor.Props
import akka.actor.SupervisorStrategy.Stop
import akka.actor.actorRef2Scala
import akka.event.LoggingReceive
import akka.actor.Terminated
import java.io.FileWriter
import java.io.File
import java.util.Calendar

case class Start
case class Write
case class FileName(name: String)

class Writer extends Actor with ActorLogging {

  import context.dispatcher

  var count = 0
  var file: File = _
  def receive = LoggingReceive {
    case Start => context.system.scheduler.schedule(Duration.Zero, Duration(2, "seconds"), self, Write)
    case Write => count = count + 1; writeToFile(count)
    case FileName(name: String) => file = new File(name) 
    case _ => println("Unknown Message")
  }

  def writeToFile(count: Int): Unit = {
    val writer = new FileWriter(file, true)
    writer.write(Calendar.getInstance().getTime() + " : " + context.system.name + " actor " + count + "\n")
    writer.close()
  }
}

class SuperWriter extends Actor with ActorLogging {

  var writer: ActorRef = _
  var remoteSuper: ActorRef = _

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = Duration(10, "seconds")) {
      case _: FileNotFoundException => self ! PoisonPill; Stop
    }

  def receive = LoggingReceive {
    case Start =>
      context.system.name match {
        case "CloudPrimary" => println("Starting Writer"); startWriter

        case "CloudBackup" =>
          remoteSuper = context.actorFor("akka://CloudPrimary@210.210.125.149:2558/user/superw")
          context.watch(remoteSuper)
          println("Watching remoteSuper....")
      }
    case Terminated(remoteSuper) => println("Received Terminated message"); startWriter
    case _ => println
  }
  
  def startWriter() = {
      writer = context.actorOf(Props[Writer], name = "writer")
      writer ! FileName("/tmp/" + context.system.name + ".log")
      writer ! Start
  }
}

object CloudWatcher {

  def main(args: Array[String]) {
    val system = ActorSystem(args(0), ConfigFactory.load().getConfig(args(0)))
    val superw = system.actorOf(Props[SuperWriter], name = "superw")
    superw ! Start
  }
}