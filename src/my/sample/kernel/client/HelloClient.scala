package my.sample.kernel.client

import akka.kernel.Bootable
import akka.actor.ActorSystem
import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.ActorRef

case class Start()

class Client extends Actor {

  var server: ActorRef = _
  override def preStart = {
    server = context.actorFor("akka://helloserver@210.210.122.100:2553/user/server")
    println(server)
  }

  def receive = {
    case Start => println("case Start"); server ! "hello server: from client"
    case m: String => println("client received: " + m)

  }
}
class HelloClient extends Bootable {

  val system = ActorSystem("helloclient", ConfigFactory.load().getConfig("HelloClient"))

  def startup = {
    val actor = system.actorOf(Props[Client], "client")
    actor ! Start
  }

  def shutdown = {
    system.shutdown
  }

}