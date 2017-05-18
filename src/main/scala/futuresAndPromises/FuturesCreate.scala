package futuresAndPromises

/**
  * Created by christian on 18/05/17.
  */
import scala.concurrent._
import ExecutionContext.Implicits.global
object FuturesCreate extends App {
  Future { log("the future is here") }
  log("the future is coming")
  Thread.sleep(1000)
}
