package my.sample.collections

import scala.collection.mutable.HashMap
import collection.JavaConversions._

object JSConversions {

  def main(args: Array[String]): Unit = {

    val mymap = HashMap[String, Object]()
    
    mymap += "test" -> "murali"
    mymap += "test1" -> "murali1"
    mymap += "test2" -> "murali2"
    
    printMap(mymap)
    
/*    val m:java.util.Map[String, java.lang.Object] = HashMap("1" -> 1.asInstanceOf[Object], "2" -> 2.asInstanceOf[Object])
    println(m)*/
    
  }
  
  def printMap (inmap: java.util.Map[String, Object]): Unit = {
    
    println(inmap)
    
  }

}