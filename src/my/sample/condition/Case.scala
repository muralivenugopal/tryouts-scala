package my.sample.condition

object Case {

  def main(args: Array[String]): Unit = {
    
    val dtype = "qos"
      
      dtype match {
      case "if" => println("if received")
      case "qos" => println("qos received")
      case "scalar" => println("scalar received")
    }
      
    
  }

}