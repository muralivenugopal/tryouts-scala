package my.sample.akka.deathwatch

import java.io.FileWriter
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import java.util.Calendar
import com.typesafe.config.ConfigFactory
import akka.actor.ActorRef
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._

case class Write
case class Hi
case class Hello
case class Sleep

object MySystem {

  def main(args: Array[String]): Unit = {
    val asname = args(0)
    val system = ActorSystem(asname, ConfigFactory.load().getConfig(args(1)))
    println(system.name)
    val writeActor = system.actorOf(Props[Writer], "writeActor")
    println("writeActor started in " + asname)
    var i = 0
    var canWrite: Boolean = true
    if (asname == "ASTwo") canWrite = false
    canWrite match {
      case true => println("start writing"); writeActor ! Write
      case false => println("nothing done")
    }

  }
}

class Writer extends Actor {
  var server: ActorRef = _

  def receive = {
    case Write => for (i <- 1 to 100) writeToFile(i.toInt); context.self ! Sleep
    case Hi => println(sender.path.name + " says Hi"); sender ! Hello
    case Hello => println(sender.path.name + " says Hello")
//    case Sleep => context.system.scheduler.scheduleOnce(Duration(1000, TimeUnit.MILLISECONDS), context.self, Write)
//    case Sleep => context.system.scheduler.scheduleOnce(2 seconds, self, Write)
  }

  override def preStart() {
    if (context.system.name == "ASTwo") {
      server = context.actorFor("akka://ASOne@localhost:2553/user/writeActor")
      server ! Hi
      println("this is a standby, and reference obtained for server")
    }
  }

  def writeToFile(count: Int): Unit = {
    val writer = new FileWriter("/tmp/AS.txt", true)
    writer.write(Calendar.getInstance().getTime() + " " + context.system.name + " actor " + count + "\n")
    writer.close()
  }
}
