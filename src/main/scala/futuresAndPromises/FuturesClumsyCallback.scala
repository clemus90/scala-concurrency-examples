package futuresAndPromises

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io.Source

import java.io._
import org.apache.commons.io.FileUtils._
import collection.JavaConverters._

/**
  * Created by clemus on 5/18/2017.
  */
object FuturesClumsyCallback extends App{
  def blacklistFile(name: String) : Future[List[String]] = Future {
    val lines = Source.fromFile(name).getLines
    lines.filter(x => !x.startsWith("#") && !x.isEmpty).toList
  }

  def findFiles(patterns: List[String]): List[String] = {
    val root = new File(".")
    for {
      f <- iterateFiles(root, null, true).asScala.toList
      pat <- patterns
      abspat = root.getCanonicalPath + File.separator + pat
      if f.getCanonicalPath.contains(abspat)
    } yield f.getCanonicalPath
  }

  blacklistFile(".gitignore") foreach {
    case lines =>
      val files = findFiles(lines)
      log(s"matches: ${files.mkString("\n")}")
  }

  def blacklisted(name: String): Future[List[String]] =
    blacklistFile(name).map(patterns => findFiles(patterns))

  Thread.sleep(10000)
}
