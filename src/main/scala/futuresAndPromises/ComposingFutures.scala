package futuresAndPromises

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io.Source
/**
  * Created by clemus on 5/18/2017.
  */
object ComposingFutures extends App{
  val netiquetteUrl = "http://www.ietf.org/rfc/rfc1855.txt"
  val netiquette = Future { Source.fromURL(netiquetteUrl).mkString }
  val urlSpecUrl = "http://www.w3.org/Addressing/URL/url-spec.txt"
  val urlSpec = Future { Source.fromURL(urlSpecUrl).mkString }
  val answer = netiquette.flatMap { nettext =>
    urlSpec.map { urltext =>
      "Check this out: " + nettext + ". And check out: " + urltext
    }
  }
  answer foreach { case contents => log(contents) }
  Thread.sleep(2000)

  val answerFor = for{
    nettext <- netiquette
    urltext <- urlSpec
  } yield {
    "First, read this: " + nettext + ". Now, try this: " + urltext
  }

  answerFor foreach { case contents => log(contents) }
  Thread.sleep(2000)

  // Similar but not the same
  val answerSeq = for {
    nettext <- Future { Source.fromURL(netiquetteUrl).mkString }
    urltext <- Future { Source.fromURL(urlSpecUrl).mkString }
  } yield {
    "First, read this: " + nettext + ". Now, try this: " + urltext
  }
}
