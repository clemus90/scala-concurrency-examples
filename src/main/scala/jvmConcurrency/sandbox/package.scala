package jvmConcurrency

/**
  * Created by clemus on 5/15/2017.
  */
package object sandbox {
  def log(msg: String): Unit = {
    println(s"${Thread.currentThread.getName}> $msg")
  }

  def thread(body: =>Unit): Thread = {
    val t = new Thread {
      override def run() = body
    }
    t.start()
    t
  }
}
