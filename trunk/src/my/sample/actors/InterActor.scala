package my.sample.actors

import scala.actors.Actor

object InterActor {

  class Sender (receiver: Actor)extends Actor {
    def act() {
        receive {
          case s: String => println("Sender received = " + s) ; receiver ! "Sender sent: hi got message"
        }
    }

  }

  class Receiver extends Actor {
    def act() {
        receive {
          case s: String => println("Receiver received = " + s); sender ! "Receiver sent: hi got message"
        }
    }
  }

  def main(args: Array[String]): Unit = {
    val receive = new Receiver
    val send = new Sender(receive)
    
    send.start
    receive.start

    send ! "send"
    receive ! "receive"
  }
}