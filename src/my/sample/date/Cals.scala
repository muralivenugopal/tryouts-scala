package my.sample.date

import java.util.Calendar
import java.util.Date

object Cals {

  def main(args: Array[String]): Unit = {

    println(1359443336L)
    println
    val date = new Date(1359443336)
    val cal: Calendar = Calendar.getInstance

    println(cal.getTime)

    cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
    println(cal.getTime)
    cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
    println(cal.getTime)

    println(cal.getTimeInMillis())

  }
}