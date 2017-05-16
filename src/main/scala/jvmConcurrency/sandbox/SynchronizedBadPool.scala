package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/16/2017.
  */
import scala.collection.mutable

object SynchronizedBadPool extends App{
  private val tasks = mutable.Queue[() => Unit]()

  val worker = new Thread {
    def poll(): Option[() => Unit] = tasks.synchronized{
      if(tasks.nonEmpty) Some(tasks.dequeue()) else None
    }

    override def run(): Unit = while(true) poll() match {
      case Some(task) => task()
      case None =>
    }
  }

  worker.setName("Worker")
  worker.setDaemon(true)
  worker.start()

  def asynchronous(body: =>Unit) = tasks.synchronized{
    tasks.enqueue(() => body)
  }
  asynchronous(log("Hello"))
  asynchronous(log(" world!"))
  Thread.sleep(5000)
}
