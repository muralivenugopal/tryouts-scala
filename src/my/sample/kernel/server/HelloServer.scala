package my.sample.kernel.server

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.kernel.Bootable
import akka.actor.Actor
import akka.actor.Props

case class test()

class Server extends Actor {
  def receive = {
    case s: String => println("server received: " + s); sender ! "server response" 
    case test => println("server actor received")
  }
  
}

class HelloServer extends Bootable {

  val system = ActorSystem("helloserver", ConfigFactory.load().getConfig("HelloServer"))
  val actor = system.actorOf(Props[Server], "server")
  
  def startup = {
    actor ! test
  }
  
  def shutdown = {
  }
}