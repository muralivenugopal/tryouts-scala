package my.sample.cascals

import com.shorrockin.cascal.session.PoolParams
import com.shorrockin.cascal.session.SessionPool
import com.shorrockin.cascal.session.Host
import com.shorrockin.cascal.session.Consistency
import com.shorrockin.cascal.session.ExhaustionPolicy
import com.shorrockin.cascal.session.Operation
import com.shorrockin.cascal.session.Insert
import com.shorrockin.cascal.model.Column
import java.nio.ByteBuffer
import com.shorrockin.cascal.utils.Conversions._
import com.shorrockin.cascal.utils.Conversions


object Lists {

  def main(args: Array[String]): Unit = {
/*
    val hosts = Host("localhost", 9160, 250) :: Nil
    val params = new PoolParams(10, ExhaustionPolicy.Fail, 500L, 6, 2)
    val pool = new SessionPool(hosts, params, Consistency.One)
        var a:Seq[Int] = Nil
    for ( i<-10 to 1 by -1 )a = i::a

    pool.borrow { session =>
      val cf = "beacon" \ "PollDataCf"

      val colPath = "beacon" \ "PollDataCf" \ "100/1234"
      println(session.list(colPath) foreach { col => println(long(col.name) + " - " + stringIfPossible(col.value)) })

            val col1 = colPath \ ("123456789", "97000")
      val col2 = colPath \ ("123456788", "96000")
      val col3 = colPath \ ("123456785", "95000")

      val test = Insert(col1) :: Insert(col2) :: Insert(col3)
      println(test.getClass().toString())
      
      list
      list ::= Insert(k)
      
      val test1 = Insert(col1) :: Insert(col2) :: Insert(col3)

            var test2 = new scala.collection.mutable.MutableList[Operation]
      test2 += Insert(colPath \ ("123456789", "97000"))
      test2 += Insert(colPath \ ("123456788", "96000"))
      test2 += Insert(colPath \ ("123456785", "95000"))
      session.batch(test2)

    }*/
  }

  private def stringIfPossible(a: ByteBuffer): String = {
    if (a == null) return "NULL"
    if (a.array.length <= 4) return "Array (" + a.array.mkString(", ") + ")"
    if (a.array.length > 1000) return a.array.toString
    try { Conversions.string(a) } catch { case _ => a.array.toString }
  }

}