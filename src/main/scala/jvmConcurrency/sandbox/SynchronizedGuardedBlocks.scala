package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/16/2017.
  */
object SynchronizedGuardedBlocks extends App{
  val lock = new AnyRef
  var message: Option[String] = None
  val greeter = thread {
    lock.synchronized {
      while (message == None) lock.wait()
      log(message.get)
    }
  }
  lock.synchronized {
    message = Some("Hello!")
    lock.notify()
  }
  greeter.join()
}
