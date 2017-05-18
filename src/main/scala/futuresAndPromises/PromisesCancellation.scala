package futuresAndPromises

import scala.concurrent._
import ExecutionContext.Implicits.global
/**
  * Created by clemus on 5/18/2017.
  */
object PromisesCancellation extends App{
  type Cancellable[T] = (Promise[Unit], Future[T])
}
