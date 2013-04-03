package my.sample.akka.sockets

import java.io._
import java.net.{InetAddress,ServerSocket,Socket,SocketException}
import java.util.Random

/** 
 * Simple client/server application using Java sockets. 
 * 
 * The server simply generates random integer values and 
 * the clients provide a filter function to the server 
 * to get only values they interested in (eg. even or 
 * odd values, and so on). 
 */
object SplunkTcpClient {
     
  def main(args: Array[String]) {
    val filter: String = "The server simply generates random integer values and the clients provide a filter function to the server to get only values they interested in eg. even or odd values, and so on" 

    try {
      val ia = InetAddress.getByName("localhost")
      val socket = new Socket(ia, 9999)
      val out = new ObjectOutputStream(
        new DataOutputStream(socket.getOutputStream()))

      filter.split(" ").foreach(f=> out.writeObject(f + "\n"))
      out.flush()

      out.close()
      socket.close()
    }
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

}