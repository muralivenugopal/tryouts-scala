package my.sample.string

object Replace {

  def main(args: Array[String]): Unit = {
    
    val text = "List(1.3.6.1.2.1.2.2.1.10, 1.3.6.1.2.1.2.2.1.16)"
      println((text.toString().replaceAllLiterally("List(", "")).replaceAllLiterally(")", ""))
    
  }

}