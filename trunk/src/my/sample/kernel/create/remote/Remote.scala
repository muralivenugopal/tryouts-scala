package my.sample.kernel.create.remote

import akka.kernel.Bootable
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

class Remote extends Bootable {
  
  var system: ActorSystem = _
  
  def startup() {
    system = ActorSystem("RemoteApp", ConfigFactory.load().getConfig("RemoteApp"))
  }
  
  def shutdown() {
    
  }

}