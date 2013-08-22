package my.sample.akka.deathwatch.cloud

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.event.LoggingReceive
import scala.concurrent.duration.Duration._
import java.io.FileWriter
import java.util.Calendar
import akka.actor.Props
import akka.actor.actorRef2Scala
import java.io.File
import scala.concurrent.duration.Duration
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import akka.dispatch.Resume
import java.io.FileNotFoundException
import akka.actor.ActorLogging


class ParentWriter extends Actor with ActorLogging {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = Duration(10, "seconds")) {
      case _: FileNotFoundException => Restart
      case _: Exception => Escalate
    }

  val writer = context.actorOf(Props[Writer], name = "writer")

  def receive = LoggingReceive {
    case Start => writer ! Start
    case _ => println
  }
}

object CloudSupervisor {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.parseString("""
        BeaconSystem {
        
    		akka.loglevel = "DEBUG"
    		akka.actor.debug {
    			receive = on
    			lifecycle = on
    		}

        	akka {
        	  	actor {
        	    	provider = "akka.remote.RemoteActorRefProvider"
        	  	}
        	remote
        	 {
        	    transport = "akka.remote.netty.NettyRemoteTransport"
        	    netty {
        	      hostname = "210.210.125.149"
        	      port = 2552
        	    }
        	  }
        	}
        }
        """)

    val config1 = ConfigFactory.parseString("""
    		akka.loglevel = "DEBUG"
    		akka.actor.debug {
    			receive = on
    			lifecycle = on
    		}
    		""")

    val system = ActorSystem("CloudWatch", config.getConfig("BeaconSystem"))
    val parent = system.actorOf(Props[ParentWriter], name = "parent")
    parent ! Start
  }

}