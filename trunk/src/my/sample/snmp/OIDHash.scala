package my.sample.snmp

import org.snmp4j.smi.OID

object OIDHash {

  def main(args: Array[String]): Unit = {
    
    val oid:OID = new OID(args(0))
    println(oid.hashCode())
    
  }

}