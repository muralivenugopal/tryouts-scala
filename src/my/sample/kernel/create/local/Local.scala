package my.sample.kernel.create.local

import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Address
import akka.actor.Deploy
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.kernel.Bootable
import akka.remote.RemoteScope
import akka.actor.AddressFromURIString

case class Start()
case class Stop()
case class Undeploy()

class client extends Actor {

  val address = Address("akka", "RemoteApp", "210.210.122.100", 8555)
  //  val address = AddressFromURIString(akka://remoteDepApp@210.210.122.100:8555")
  var remoteActor = context.actorOf(Props[server].withDeploy(Deploy(scope = RemoteScope(address))))

  override def preStart = {
    println("starting client")
  }

  override def postStop = {
    println("shutting client")
  }

  def receive = {
    case Start => remoteActor ! "Hi remote"
    case m: String => println("client: message received : " + m)
  }
}

class server extends Actor {

  override def preStart = {
    println("starting server")
  }

  override def postStop = {
    println("shutting server")
  }

  def receive = {
    case m: String => println("server: message received : " + m) ; sender ! "Hi local"
  }
}

class Local extends Bootable {

  var localActor: ActorRef = _
  var system: ActorSystem = _

  def startup = {
    system = ActorSystem("LocalApp", ConfigFactory.load().getConfig("LocalApp"))
    val localActor = system.actorOf(Props[client], "localActor")
    localActor ! Start
    localActor ! "send"

  }

  def shutdown = {

  }

}