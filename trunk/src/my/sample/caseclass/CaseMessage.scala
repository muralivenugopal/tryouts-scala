package my.sample.caseclass

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

case class Message(fname: String, lname: String)
case class Person (fname: String, age: Int, mobile: Map[Int, Int])

class Client extends Actor {
  def receive = {
//    case server:ActorRef => server ! Message ("murali", "venugopal")  
    case server:ActorRef => server ! Person("murali", 30, Map(1 -> 9, 2 -> 5, 3 -> 0, 4 -> 0, 5 -> 0, 6 -> 0, 7 -> 3, 8 -> 5, 9 -> 8, 10 -> 0))
  }
}

class Server extends Actor {
  def receive = {
    case Message(fname, lname) => println("hi:" + lname + ", " + fname)
    case Person(fname, age, mobile) => { 
     println(fname + "," + age)
     println(mobile)
    } 
  }
}

object CaseMessage {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("caseSys", ConfigFactory.load().getConfig("CaseServer"))
    val serverAct = system.actorOf(Props[Server], "Server")
    val clientAct = system.actorOf(Props[Client], "client")
    
    clientAct ! serverAct
  }
}