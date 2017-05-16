package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/16/2017.
  */
import scala.collection.mutable

object SynchronizedPool extends App{
  private val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {
    setDaemon(true)

    def poll(): () => Unit = tasks.synchronized{
      while(tasks.isEmpty) tasks.wait()
      tasks.dequeue()
    }

    override def run():Unit = while(true){
      val task = poll()
      task()
    }
  }

  Worker.start()

  def asynchronous(body: =>Unit) = tasks.synchronized{
    tasks.enqueue(() => body)
    tasks.notify()
  }
  asynchronous(log("Hello"))
  asynchronous(log(" world!"))
  Thread.sleep(500)
}
