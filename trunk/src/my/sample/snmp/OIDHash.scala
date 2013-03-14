package my.sample.snmp

import org.snmp4j.smi.OID

object OIDHash {

  def main(args: Array[String]): Unit = {
    
//    val oid:OID = new OID(args(0))
    val oid:OID = new OID("1.3.6.1.2.1.25.2.3.1.5.35")
    println(oid.hashCode())
    
  }

}