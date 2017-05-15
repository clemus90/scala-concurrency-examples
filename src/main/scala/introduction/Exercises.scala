package introduction

/**
  * Created by christian on 14/05/17.
  */
object Exercises {
  /**
    * Exercise 1: implement a compose method
    */
  def compose[A,B,C](g: B => C, f: A => B): A => C = (x: A) => g(f(x))

  /**
    * Exercise 2: implement fuse with for comprehension
    */
  def fuse[A, B](a: Option[A], b:Option[B]): Option[(A,B)] = {
    for{
      x <- a
      y <- b
    } yield (x,y)
  }

  /**
    * Exercise 3: check, which return true if every position in a sequence
    * comply with second parameter
    */
  def check[T](xs: Seq[T])(pred: T => Boolean): Boolean = {
    xs.foldLeft(true)((running: Boolean, current: T) => {
      running && (try {
        pred(current)
      } catch {
        case _: Exception => false
      })
    })
  }

  /**
    * Exercise 4: Modify Pair to be usable with pattern matching
    */
  case class Pair[P, Q](first: P, second: Q)

  /**
    * Exercise 5: lexicographic permutations of a string
    */
  def permutations(x: String): Seq[String] = x match {
    case "" => Nil
    case _ => (0 to x.length).flatMap(p => permutations(x.take(p) ++ x.drop(p+1)).map(w => x(p) + w))
  }

}
