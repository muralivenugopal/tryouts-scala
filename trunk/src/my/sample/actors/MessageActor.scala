package my.sample.actors

import scala.actors.Actor._
import scala.actors.Actor

object MessageActor {

  def main(args: Array[String]): Unit = {

    //Matching sample based on types

    val tests = List(30, "murali", 5.9, 'q')

    for (test <- tests) {
      test match {
        case i: Int => println("got an int")
        case s: String => println("got a string")
        case _ => println("oops")
      }
    }

    val test = actor {
      loop {
        receive {
          case i: Int => println("actor got a number")
          case s: String => println("actor got a string")
        }
      }
    }

    test.start
    
    test ! 12
    test ! "murali"

  }

}