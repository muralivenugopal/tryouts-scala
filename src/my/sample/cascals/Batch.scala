package my.sample.cascals

import com.shorrockin.cascal.session.Consistency
import com.shorrockin.cascal.session.ExhaustionPolicy
import com.shorrockin.cascal.session.Host
import com.shorrockin.cascal.session.Insert
import com.shorrockin.cascal.session.PoolParams
import com.shorrockin.cascal.session.SessionPool
import com.shorrockin.cascal.utils.Conversions._
import com.shorrockin.cascal.session.Operation
import java.util.Date

object Batch {

  def main(args: Array[String]): Unit = {

    val hosts = Host("localhost", 9160, 250) :: Nil
    val params = new PoolParams(10, ExhaustionPolicy.Fail, 500L, 6, 2)
    val pool = new SessionPool(hosts, params, Consistency.One)
    /*    var a:Seq[Int] = Nil
    for ( i<-10 to 1 by -1 )a = i::a*/

    pool.borrow { session =>
      val cf = "beacon" \ "PollDataCf"

      val colPath = "beacon" \ "PollDataCf" \ "100/1234"

      /*      val col1 = colPath \ ("123456789", "97000")
      val col2 = colPath \ ("123456788", "96000")
      val col3 = colPath \ ("123456785", "95000")

      val test = Insert(col1) :: Insert(col2) :: Insert(col3)
      println(test.getClass().toString())
      
      list
      list ::= Insert(k)
      
      val test1 = Insert(col1) :: Insert(col2) :: Insert(col3)*/

      var test2 = new scala.collection.mutable.MutableList[Operation]
      /*test2 += Insert(colPath \ (1357642648, 970001234))
      test2 += Insert(colPath \ (1357642248, 960001234))
      test2 += Insert(colPath \ (1234527846, 950001234))
      */
      
      test2 += Insert(colPath \ (new Date(), long(970001234)))
      /*test2 += Insert(colPath \ (date(1357642248), long(960001234)))
      test2 += Insert(colPath \ (date(1234527846), long(950001234)))
      */
/*      test2 += Insert(colPath \ (date(new Date()), 970001234))
      test2 += Insert(colPath \ (date(new Date()), 960001234))
      test2 += Insert(colPath \ (date(new Date()), 950001234))*/
      
      
      
      
      

      session.batch(test2)
    }
  }
}