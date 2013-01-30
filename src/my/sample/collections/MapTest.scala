package my.sample.collections

object MapTest {

  def main(args: Array[String]): Unit = {
    
    val test = List(1::2::3::5::Nil)
    println(test.map(obj => (obj, obj + 1.toString)) toMap)
//    println(test.map(obj => obj foreach {o => (o, o + 1.toString())}) toMap)
    
    val tmap = Map() ++ (test map(o => (o, o + 1.toString)))
    println(tmap)
    
//    val tmap1 = test.map(obj => )
    
    case class Data(a: Int, b: Int)
    
    val list = List(Data(1,2)::Data(3,4)::Data(5,6)::Data(7,8)::Nil)
    
    println(list)
    list foreach{data => println(data)}
    
    
  }

}