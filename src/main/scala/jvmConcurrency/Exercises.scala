package jvmConcurrency

import scala.runtime.Nothing$

/**
  * Created by christian on 16/05/17.
  */
object Exercises extends App{
  /**
    * 1) parallel, using two threads
    */
  def parallel[A, B](a: =>A, b: =>B): (A,B) = {
    var retA: Option[A] = None
    var retB: Option[B] = None
    val t1 = thread {retA = Some(a)}
    val t2 = thread {retB = Some(b)}
    t1.join()
    t2.join()
    (for(x <- retA; y <- retB) yield (x,y)) match {
      case Some((x0,y0)) => (x0,y0)
      case None => throw new Exception("Computation not finished successfully")
    }
  }

  /**
    * 2) Periodic execution of a computation
    */

  def periodically(duration: Long)(b: => Unit): Unit = {
    thread {b}
    Thread.sleep(duration)
    periodically(duration)(b)
  }

  /**
    * 3) SyncVar to share data between threads
    */

  class SyncVar[T] {
    var v: Option[T] = None
    def get(): T = v.synchronized {
      val ret = v match {
        case Some(x) => x
        case None => throw new Exception("Get over empty var")
      }
      v = None
      ret
    }

    def put(x: T): Unit = v.synchronized {
      v match {
        case Some(x) => throw new Exception("There is already a value stored")
        case None => v = Some(x)
      }
    }

  }

}
