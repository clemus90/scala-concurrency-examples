package introduction

/**
  * Created by christian on 14/05/17.
  */
object Exercises extends App{
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
  def permutations(str: String): Seq[String] = {
    //println(s"DEBUG $str")
    (str match {
      case x if x.length == 0 => Seq("")
      case _ => (0 until str.length).flatMap(p => permutations(str.take(p) ++ str.drop(p+1)).flatMap(w => Seq(str(p) + w, w)))
    }).toSet.toSeq.sorted
  }

  /**
    * Exercise 6: Combinations of length n
    */
  def combinations(n:Int, xs:Seq[Int]): Iterator[Seq[Int]] = n match {
    case 0 => Iterator(Seq())
    case x => (0 until xs.length).map(i =>
      combinations(n-1, xs.take(i) ++ xs.drop(i+1)).map(comb =>
        xs(i) +: comb)).toIterator.flatten
  }

  /**
    * Exercise 7: Regular Expression matcher
    */
  def matcher (regex: String): PartialFunction[String, List[String]] = {
    val regObj = regex.r
    return PartialFunction( (x:String) => regObj.findAllMatchIn(x) match {
      case lst if lst.hasNext => lst.toList.map(_.group(0))
    })
  }

  //TESTING
  //1.
  println(1)
  val sum = (x:(Int,Int)) => x._1 + x._2
  val multByTwo = (m: Int) => m * 2
  val comp = compose[(Int, Int), Int, Int](multByTwo, sum)

  println(multByTwo(sum(10, 5)) == comp(10,5))

  //2.
  println(2)
  println(fuse(Some(1), Some(2)))
  println(fuse(Some(1), None))
  println(fuse(None, Some(2)))

  //3.
  println(3)
  println(check(Seq(1,2,3,4,5,6,7))(_ < 10))
  println(check(Seq(1,2,3,4,5,6,7))(_ != 4))

  //4.
  println(4)
  Pair(1, 2) match {
    case Pair(x,y) => println (s"the pair content is x: $x y: $y")
  }

  //5.
  println(5)
  for (x <- permutations("abcd"))println(x)

  //6.
  println(6)
  for{
    i <- 2 to 4
    x <- combinations(i, Seq(10,20,30,40))
  }{
    println(s"$i combination $x")
  }

  //7.
  println(7)
  val toMatch = matcher("[A-Za-z]{2}")
  for(x <- toMatch("Christian")) println(x)
}
