package futuresAndPromises

/**
  * Created by christian on 18/05/17.
  */
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io.Source
object FuturesFailure extends App {
  val urlSpec: Future[String] = Future {
    val invalidUrl = "http://www.w3.org/non-existent-url-spec.txt"
    Source.fromURL(invalidUrl).mkString
  }
  urlSpec.failed foreach {
    case t => log(s"exception occurred - $t")
  }
  
  Thread.sleep(1000)
}
