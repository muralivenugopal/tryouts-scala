package my.sample.xml

import scala.xml.XML

object WriteXML {

  def main(args: Array[String]): Unit = {
    val test = <name><value type="first">Murali</value><value type="last">Venugopal</value></name>
//    def write = XML.save("/tmp/test.xml", test, "UTF-8", false, null)
    scala.xml.XML.save("/tmp/test.xml", test, "UTF-8", true, null)
    println("done")
  }
  
}