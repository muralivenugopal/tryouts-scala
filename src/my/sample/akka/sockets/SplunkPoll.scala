package my.sample.akka.sockets

import java.io._
import java.net.{ InetAddress, ServerSocket, Socket, SocketException }
import java.util.Random
import org.snmp4j.PDU
import org.snmp4j.Snmp
import org.snmp4j.CommunityTarget
import org.snmp4j.transport.DefaultUdpTransportMapping
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.smi.Integer32
import org.snmp4j.smi.OctetString
import org.snmp4j.smi.VariableBinding
import org.snmp4j.smi.UdpAddress
import org.snmp4j.smi.OID

/**
 * Simple client/server application using Java sockets.
 *
 * The server simply generates random integer values and
 * the clients provide a filter function to the server
 * to get only values they interested in (eg. even or
 * odd values, and so on).
 */
object SplunkPoll {

  def main(args: Array[String]) {
      
    try {
      val ia = InetAddress.getByName("localhost")
      val socket = new Socket(ia, 9998)
      val out = new ObjectOutputStream(
        new DataOutputStream(socket.getOutputStream()))

      val output=snmpGet
      out.writeObject(output)
      out.flush()

      out.close()
      socket.close()
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

  def snmpGet(): String = {

    val transport = new DefaultUdpTransportMapping()
    val cTarget = new CommunityTarget()

    cTarget.setCommunity(new OctetString("public"))
    cTarget.setVersion(SnmpConstants.version2c)
    cTarget.setAddress(new UdpAddress(InetAddress.getByName("210.210.122.100"), 161))

    transport.listen()

    val pdu = new PDU()
    pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.2.2.1.10.2")))
    pdu.setRequestID(new Integer32(1005))
    pdu.setType(PDU.GET)

    val snmp = new Snmp(transport)
    val response = snmp.getNext(pdu, cTarget)

    if (response != null)
      response.getResponse().getVariableBindings().firstElement().getOid().hashCode().toString + "=" + response.getResponse().getVariableBindings().firstElement().getVariable().toString
    else
      "Null"
  }

}