package my.sample.akka.deathwatch.cloud

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.event.LoggingReceive
import scala.concurrent.duration.Duration._
import java.io.FileWriter
import java.util.Calendar
import akka.actor.Props
import akka.actor.actorRef2Scala
import java.io.File
import scala.concurrent.duration.Duration
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import akka.dispatch.Resume
import java.io.FileNotFoundException

case class Start
case class Write

class Writer extends Actor {

  import context.dispatcher

  var count = 0
  val file = new File("/tmp/writer.log")
  def receive = LoggingReceive {
    case Start => context.system.scheduler.schedule(Duration.Zero, Duration(2, "seconds"), self, Write)
    case Write =>
      count = count + 1; writeToFile(count)
    case _ => println("Unknown Message")
  }

  def writeToFile(count: Int): Unit = {
    val writer = new FileWriter(file, true)
    writer.write(Calendar.getInstance().getTime() + " : " + context.system.name + " actor " + count + "\n")
    writer.close()
  }
}

class ParentWriter extends Actor {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = Duration(10, "seconds")) {
      case _: FileNotFoundException => Restart 
      case _: Exception => Escalate
    }

  val writer = context.actorOf(Props[Writer], name = "writer")

  def receive = LoggingReceive {
    case Start => writer ! Start
    case _ => println
  }
}

object CloudWatchSystem {
  def main(args: Array[String]): Unit = {

    val config1 = ConfigFactory.parseString("""
    		akka.loglevel = "DEBUG"
    		akka.actor.debug {
    			receive = on
    			lifecycle = on
    		}
    		""")

    val system = ActorSystem("CloudWatch", config1)
    val parent = system.actorOf(Props[ParentWriter], name = "parent")
    parent ! Start
  }

}