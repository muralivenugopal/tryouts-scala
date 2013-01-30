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

object BasicOps {

  def main(args: Array[String]): Unit = {
    val hosts = Host("localhost", 9160, 250) :: Nil
    val params = new PoolParams(10, ExhaustionPolicy.Fail, 500L, 6, 2)
    val pool = new SessionPool(hosts, params, Consistency.One)

    pool.borrow { session =>
/*      val colPath = "beacon" \ "PollDataCf" \ "10/1234"
      println(colPath.getClass().toString())
      session.insert(colPath \ (new Date().getTime(), 192))

      val getCol = "beacon" \ "PollDataCf" \ "10/1234" \ "1234567890" 
      val col1 = session.get(getCol)
      System.out.println("Stored value col1: " + string(col1.get.value))*/
      
      
      //date sample cf
      
//      val datePath = "beacon" \ "test" \ "userID1"
      val currDate = new Date(113, 1, 9, 10, 20, 00)
      
      val longVal: Long = 1005
      
/*      println("val is: " + currDate + " in " + currDate.getClass.toString())
      session.insert(datePath \ ("dob", currDate))
      
      val colGetU = datePath \ "dob"
      val colGet = session.get(colGetU)
      
      println(long(colGet.get.value))*/
      
      //date for beacon sample cf
      
      val datePath1 = "beacon" \ "mytest1" \ "10/1234"
      
      println("val is: " + currDate + " in " + currDate.getClass.toString())
      session.insert(datePath1 \ (currDate, longVal))
      
      
      val colGetU = datePath1 \ currDate 
      val colGet = session.get(colGetU)
      
      println(date(colGet.get.name) + " : " + long(colGet.get.value))
//      println(string(colGet.get.value))
      
      
/*      println("sliced")
      val res = session.list(datePath1, RangePredicate(new Date(113, 1, 9, 10, 05, 00), new Date(113, 1, 9, 10, 15, 00)))
      res foreach {o => println(date(o.name).toString()+ " : " + string(o.value))}
      println()
      println()
      println("full list")
      val res1 = session.list(datePath1)
      res1 foreach {o => println(date(o.name).toString()+ " : " + string(o.value))}
      println(res1.size)*/
    }
  }
}

/*      val getCol = "beacon" \ "PollDataCf" \ "876" \ "123456789"
      
      Exception in thread "main" java.util.NoSuchElementException: None.get
	at scala.None$.get(Option.scala:274)
	at scala.None$.get(Option.scala:272)
	at my.sample.cascals.BasicOps$$anonfun$main$1.apply(BasicOps.scala:29)
	at my.sample.cascals.BasicOps$$anonfun$main$1.apply(BasicOps.scala:18)
	at com.shorrockin.cascal.session.SessionPool.borrow(SessionPool.scala:85)
	at my.sample.cascals.BasicOps$.main(BasicOps.scala:18)
	at my.sample.cascals.BasicOps.main(BasicOps.scala)
*/