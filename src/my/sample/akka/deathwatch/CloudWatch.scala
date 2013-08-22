package my.sample.akka.deathwatch

import com.typesafe.config.ConfigFactory

class CloudWatch {

}

object application {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.parseString("""
        BeaconSystem {
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
        
//        val system = ActorSystem(")

    println("hi")
  }
}