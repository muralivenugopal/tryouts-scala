package my.sample.cascals

import com.shorrockin.cascal.session.Host
import com.shorrockin.cascal.session.PoolParams
import com.shorrockin.cascal.session.SessionPool
import com.shorrockin.cascal.session.ExhaustionPolicy
import com.shorrockin.cascal.session.Consistency
import com.shorrockin.cascal.utils.Conversions._
import com.shorrockin.cascal.session._


object BasicOps2 {

  def main(args: Array[String]): Unit = {
    
    // Set up session pool
    val hosts  = Host("localhost", 9160, 250) :: Nil
    val params = new PoolParams(10, ExhaustionPolicy.Fail, 500L, 6, 2)
    val pool   = new SessionPool(hosts, params, Consistency.One)

    pool.borrow { session =>

      // Define column path
      val colPath = "Keyspace1" \ "Standard1" \ "key" \ "column-name"

      // insert
      session.insert(colPath \ "value")

      // read
      val col = session.get(colPath)
      System.out.println("Stored value: " + string(col.get.value))
    }
  }

}