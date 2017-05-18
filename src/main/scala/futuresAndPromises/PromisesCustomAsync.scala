package futuresAndPromises

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.control.NonFatal
/**
  * Created by clemus on 5/18/2017.
  */

object PromisesCustomAsync extends App {
  def myFuture[T](b: =>T): Future[T] = {
    val p = Promise[T]
    global.execute(new Runnable {
      def run() = try {
        p.success(b)
      } catch {
        case NonFatal(e) => p.failure(e)
      }
    })
    p.future
  }
  val f = myFuture { "naa" + "na" * 8 + " Katamari Damacy!" }
  f foreach { case text => log(text) }
}