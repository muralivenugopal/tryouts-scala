package my.sample.collections

import scala.collection.mutable.{LinkedList => MList}

object ListSamples {

  def main(args: Array[String]): Unit = {
    
    val list = List(1,2,3)
    println(list + " " + list.hashCode)
    
    val list1 = 4 :: list
    println(list1 + " " + list1.hashCode)
    
    val list2 = 5 +: list1
    println(list2 + " " + list2.hashCode)
    
    val list3 = List (7,8,9)
    
    val list4 = list3 ++ list2
    println(list4 + " " + list4.hashCode)
    
    val list5 = MList(10,11,12)
    println(list5 + " " + list5.hashCode)
  }
  

}