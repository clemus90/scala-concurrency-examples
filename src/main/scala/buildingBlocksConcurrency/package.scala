/**
  * Created by clemus on 5/17/2017.
  */

import scala.concurrent._

package object buildingBlocksConcurrency {
  def log(msg: String): Unit = {
    println(s"${Thread.currentThread.getName}> $msg")
  }

  def execute(body: =>Unit) = ExecutionContext.global.execute(
    new Runnable {
      override def run(): Unit = body
    }
  )
}
