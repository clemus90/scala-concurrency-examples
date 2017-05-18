/**
  * Created by christian on 17/05/17.
  */

import scala.concurrent._

package object buildingBlocksOfConcurrency {
  def log(msg: String): Unit = {
    println(s"${Thread.currentThread.getName}> $msg")
  }

  def execute(body: =>Unit) = ExecutionContext.global.execute{
    new Runnable {
      override def run(): Unit = body
    }
  }
}
