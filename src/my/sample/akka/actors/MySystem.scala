package my.sample.akka.actors

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

case class Start()
case class Stop()

object MySystem {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ASOne")
    val send = system.actorOf(Props[MySender], "send")
    
    send ! Start
  }
}

class MySender extends Actor {
  var i = 1
  val receiver = context.actorOf(Props[MyReceiver], "receiver")
  def receive = {
    case Start => while (i < 10 ) {
      receiver ! i
      i = i + 1
    }
  }
}

class MyReceiver extends Actor {
  
  def receive = {
    case m: Int => println ("myreceiver: " + m)
  }
  
}