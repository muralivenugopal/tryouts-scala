package my.sample.akka.future

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask

case class Start
case class Stop

object MyFuture {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ASOne")
//    val send = system.actorOf(Props[MySender], "send")
    
//    send ! Start
  }

}

/*class MySender extends Actor {
  var i = 1
  val receiver = context.actorOf(Props[MyReceiver], "receiver")
  def receive = {
//    case Start => val future = receiver.ask()
  }
}*/

class MyReceiver extends Actor {
  
  def receive = {
    case m: Int => Thread.sleep(1000); println ("myreceiver: " + m)
  }
  
}