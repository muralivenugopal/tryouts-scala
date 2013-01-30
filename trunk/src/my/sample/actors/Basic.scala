package my.sample.actors

import scala.actors.Actor

object Basic {

  def main(args: Array[String]): Unit = {

    val robert = new Redford
    robert.start
    val green = new Greeford
    green.start

    while (true) {
      println("hi main")
    }

  }

  class Redford extends Actor {
    def act() {
      while (true) {
        println("hi redness")
      }
    }
  }

  class Greeford extends Actor {
    def act() {
      while (true) {
        println("hi green")
      }
    }
  }

}