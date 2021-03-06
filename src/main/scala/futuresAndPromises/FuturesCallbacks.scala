package futuresAndPromises

/**
  * Created by christian on 18/05/17.
  */
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io.Source
object FuturesCallbacks extends App {
  def getUrlSpec(): Future[List[String]] = Future {
    val url = "http://www.w3.org/Addressing/URL/url-spec.txt"
    val f = Source.fromURL(url)
    try f.getLines.toList finally f.close()
  }

  val urlSpec: Future[List[String]] = getUrlSpec()

  def find(lines: List[String], keyword: String): String = lines.zipWithIndex collect {
    case (line, n) if line.contains(keyword) => (n, line)
  } mkString ("\n")

  urlSpec foreach {
    case lines => log(find(lines, "telnet"))
  }
  log("callback registered, continuing with other work")


  urlSpec foreach {
    case lines => log(find(lines, "password"))
  }
  Thread.sleep(2000)
  Thread.sleep(1000)
}