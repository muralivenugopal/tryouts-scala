package my.sample.functions

object AnonymousFunctions {

  def main(args: Array[String]): Unit = {
    var ex = (x:Int) => x * 2
    println(ex(8)-3)
    
    var ex1 = (x:Int) => (y:Int) => x*y
    println(ex1(2)(4))
    
    println(ex1(3))
  }

}