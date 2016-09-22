package my.sample.akka.actor.route

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter
import akka.routing.RandomRouter

case class Start()
case class Stop()

object Routing {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ASOne")
    val send = system.actorOf(Props[MySender], "send")
    
    send ! Start
  }

}

class MySender extends Actor {
  var i = 1
//  val receiver = context.actorOf(Props[MyReceiver].withRouter(RandomRouter(nrOfInstances = 5)).withDispatcher("PinnedDispatcher"))
  val receiver = context.actorOf(Props[MyReceiver].withRouter(RoundRobinRouter(nrOfInstances = 5)))
  def receive = {
    case Start => while (i < 30 ) {
      receiver ! i
      i = i + 1
    }
  }
}

class MyReceiver extends Actor {
  
  def receive = {
    case m: Int => println (self.path.name + " : " + m)
  }
  
}