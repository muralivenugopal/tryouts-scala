package my.sample.actors

import scala.actors.Actor._

object FactoryActor {

  def main(args: Array[String]): Unit = {
    val me = actor {
      println("hi me")
    }
    
    me.start
  }

}