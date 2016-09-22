package my.sample.xml

import scala.xml.Attribute

import scala.xml.Text
import scala.xml.MetaData
import scala.xml.UnprefixedAttribute

object Attributes {

  def main(args: Array[String]): Unit = {

    val dev = <Device name="test" ip="210.210.122.100" state="Down" status="Discovering"/>

    println(dev.attributes)
    val atts = dev.attributes.remove("status")
    println(atts)

    val atts1 = atts.append(Attribute(None, "status", Text("Discovered"), scala.xml.Null))
    println(atts1)

    val dev2 = <Device name="test" ip="210.210.122.100" state="Down" status="Discovering"/>
      val atts2 = dev2.attributes.remove("status").append(Attribute(None, "status", Text("Discovered"), scala.xml.Null))
      println("atts2 " + atts2 )

  }
}