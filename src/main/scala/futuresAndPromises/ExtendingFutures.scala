package futuresAndPromises

import scala.concurrent._
import ExecutionContext.Implicits.global
/**
  * Created by clemus on 5/18/2017.
  */
object ExtendingFutures extends App{
  implicit class FutureOps[T](val self: Future[T]) {
    def or(that: Future[T]): Future[T] = {
      val p = Promise[T]
      self onComplete { case x => p tryComplete x }
      that onComplete { case y => p tryComplete y }
      p.future
    }

    Future { 2 + 2 }.or(Future {3})
  }
}
