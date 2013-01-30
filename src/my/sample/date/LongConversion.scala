package my.sample.date

import java.util.Date

object LongConversion {

  def main(args: Array[String]): Unit = {
    
    val date = new Date(1359443336)
    
    println(date.toString)
    println(date.getTime)
    
    date.setMinutes(0)
    date.setSeconds(0)
    
    println(date.toString)
    
    println(date.getTime)
    
  }

}