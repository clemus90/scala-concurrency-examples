package futuresAndPromises

import scala.concurrent._
import ExecutionContext.Implicits.global
/**
  * Created by clemus on 5/18/2017.
  */
object PromisesCreate extends App {
  val p = Promise[String]
  val q = Promise[String]
  p.future foreach { case x => log(s"p succeeded with '$x'") }
  Thread.sleep(1000)
  p success "assigned"
  q failure new Exception("not kept")
  q.future.failed foreach { case t => log(s"q failed with $t") }
  Thread.sleep(1000)
}