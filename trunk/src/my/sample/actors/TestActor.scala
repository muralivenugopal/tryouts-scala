package my.sample.actors

object TestActor {

  abstract class Term
  case class Var(name: String) extends Term
  case class Fun(arg: String, body: Term) extends Term
  case class App(f: Term, v: Term) extends Term

  def main(args: Array[String]): Unit = {
    
    Fun("x", Fun("y", App(Var("x"), Var("y"))))
    
  }

}