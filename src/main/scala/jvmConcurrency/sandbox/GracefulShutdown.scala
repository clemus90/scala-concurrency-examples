package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/16/2017.
  */
import jvmConcurrency.sandbox.SynchronizedPool.Worker.setDaemon

import scala.collection.mutable

object GracefulShutdown extends App{
  private val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {

    var terminated = false

    def poll(): Option[() => Unit] = tasks.synchronized{
      while(tasks.isEmpty && !terminated) tasks.wait()
      if(!terminated) Some(tasks.dequeue()) else None
    }

    import scala.annotation.tailrec
    @tailrec
    override def run():Unit = poll() match {
      case Some(task) => task(); run()
      case None =>
    }

    def shutdown() = tasks.synchronized {
      terminated = true
      tasks.notify()
    }
  }

  Worker.start()
}
