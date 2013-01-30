package my.sample.actorsystem

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

object TestApp {

  def main(args: Array[String]): Unit = {
 
    val system = ActorSystem("SystemCheck", ConfigFactory.load().getConfig("clientSys"))
    
  }

}