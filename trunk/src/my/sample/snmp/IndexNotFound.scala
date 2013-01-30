package my.sample.snmp
import org.snmp4j.transport.DefaultUdpTransportMapping
import org.snmp4j.CommunityTarget
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.smi.OctetString
import org.snmp4j.Snmp
import org.snmp4j.smi.UdpAddress
import org.snmp4j.PDU
import java.net.InetAddress
import org.snmp4j.smi.VariableBinding
import org.snmp4j.smi.Integer32
import org.snmp4j.smi.OID

object IndexNotFound {

  def main(args: Array[String]): Unit = {
    
  }

  def snmpGetNext(ip: String, community: String, oid: String): Unit = {

    val transport = new DefaultUdpTransportMapping()
    val cTarget = new CommunityTarget()

    cTarget.setCommunity(new OctetString(community))
    cTarget.setVersion(SnmpConstants.version2c)
    cTarget.setAddress(new UdpAddress(InetAddress.getByName(ip), 161))

    transport.listen()
    
    val pdu = new PDU()
    pdu.add(new VariableBinding(new OID(oid)))
    pdu.setRequestID(new Integer32(1005))
    pdu.setType(PDU.GETNEXT)
    print(pdu.getType()); print(":")
    println(pdu.toString())

    val snmp = new Snmp(transport)
    val response = snmp.getNext(pdu, cTarget)

    if (response != null)
      print(response.getResponse().getVariableBindings().firstElement().getOid().toString())
    print(" : ")
    println(response.getResponse().getVariableBindings().firstElement().getVariable().toString())
  }
}