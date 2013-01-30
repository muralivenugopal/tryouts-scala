package my.sample.cascals

import com.shorrockin.cascal.session.Host
import com.shorrockin.cascal.session.PoolParams
import com.shorrockin.cascal.session.SessionPool
import com.shorrockin.cascal.session.ExhaustionPolicy
import com.shorrockin.cascal.session.Consistency
import com.shorrockin.cascal.utils.Conversions._
import com.shorrockin.cascal.session._
import java.util.Date
import java.sql.Timestamp
import java.util.Calendar

object ListBeacon {

  def main(args: Array[String]): Unit = {

    val hosts = Host("localhost", 9160, 250) :: Nil
    val params = new PoolParams(10, ExhaustionPolicy.Fail, 500L, 6, 2)
    val pool = new SessionPool(hosts, params, Consistency.One)

    pool.borrow { session =>

      /*val counterPath1 = "beacon" \# "test" \ "1" \ 10
      
//      session.insert(counterPath1 \ (10, 100))
//      session.count(counterPath1 \ (10, 100))
      session.add(counterPath1 + 2)
      */

      val colGetU = ("beacon" \# "HourDataCf" \ "102/1065")
      val colGet = session.list(colGetU)
      println(colGet.toString)
      

    }
  }
}