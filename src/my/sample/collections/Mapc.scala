package my.sample.collections

import scala.collection.immutable.List
import scala.collection.mutable.Queue

object Mapc {

  def main(args: Array[String]): Unit = {}

  val testMap = Map(
    "murali" -> "venu",
    "mani" -> "maran")

  testMap foreach { kv => println(kv._1 + ":" + kv._2) }

  val names = List("murali", "mani", "hari")
  names foreach { i => println("val=" + i) }

  val myMap = Map(
    "murali" -> "venu",
    "venu" -> "desu")
  myMap foreach (mm => println(mm._1 + ":" + mm._2))

  val map1 = scala.collection.mutable.Map[String, String]()

  map1 += "laks" -> "venu"
  map1 += "murali" -> "venu"

  println("last one: " + map1.toString())
  println(map1("murali"))
  
  
  val mapI = scala.collection.mutable.Map[Int, Long]()
  mapI += 1 -> 111111111
  mapI += 2 -> 222222222
  mapI += 3 -> 333333333
  
  println("mapI found " + mapI(1))
  
  val test = mapI.getOrElse(5,0)
  println(mapI.getOrElse(5,0))
  println(0L-1L)
  val temp = mapI.get(5)
  var temp1:Long = _
  if (temp.isEmpty) {
    temp1 = 0 - 1
  } else {
    temp1 = temp.get - 1
  }
  
  println("temp1 " + temp1)
  
  println(mapI.get(1).isEmpty)
      
  println("mapI found " + mapI(1).getClass.toString())
  
  

  def inc(count: Int): Unit = {
    //    println(count += 1)
  }

  //  def add(map: Map[String, String]): Map[String, String] = {
  //    map += "chennai" -> "city"
  //    map

  //  }

  val myList = List[Int]()

  /*  list += 10
  list += 11*/

  var buffer = new Queue[Int]

  buffer += 10

  println(buffer)
  
/*  val arr = new Array[Int](5)
  arr += 10
  arr += 11*/
//  println(arr)

}