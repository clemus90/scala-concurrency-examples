package futuresAndPromises

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io.Source
/**
  * Created by clemus on 5/18/2017.
  */
object RecoveringFuture extends App{
  val netiquetteUrl = "http://www.ietf.org/rfc/rfc1855.doc"
  val netiquette = Future { Source.fromURL(netiquetteUrl).mkString }
  val answer = netiquette recover {
    case e: java.io.FileNotFoundException =>
      "Dear secretary, thank you for your e-mail." +
        "You might be interested to know that ftp links " +
        "can also point to regular files we keep on our servers."
  }
  answer foreach { case contents => log(contents) }
  Thread.sleep(2000)
}
