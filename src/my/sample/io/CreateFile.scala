package my.sample.io

import java.io.PrintWriter
import java.io.File
import java.util.Calendar

object CreateFile {

  def main(args: Array[String]): Unit = {
    
    println("create file called in: " + this.toString())
    val fileName = "test.txt"
    val writer = new PrintWriter(new File("/tmp/" + fileName))
    writer.write("Hello murali " + Calendar.getInstance().getTime() + "\r")
    writer.close()
  }
}