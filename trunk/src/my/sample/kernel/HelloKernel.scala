package my.sample.kernel

import akka.kernel.Bootable
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor

case class Start

class HelloActor extends Actor {
  val worldActor = context.actorOf(Props[WorldActor])
  def receive = {
    case Start => worldActor ! "hello" 
    case message: String => println("HelloActor: " + message)
  }
}

class WorldActor extends Actor {
  def receive = {
//    case message: String => println("WorldActor: " + message.toUpperCase() + "kernel")
    case message: String => sender ! (message.toUpperCase() + "kernel")
  }
}

class HelloKernel extends Bootable {
  
  val system = ActorSystem("hellokernel")
  
  def startup = {
    println("starting up....")
    system.actorOf(Props[HelloActor]) ! Start
  }
  
  def shutdown = {
    println("shutting down....")
    system.shutdown
  }
  
  

}