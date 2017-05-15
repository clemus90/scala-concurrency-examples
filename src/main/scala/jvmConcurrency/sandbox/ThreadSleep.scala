package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/15/2017.
  */
object ThreadSleep extends App{

  val t = thread{
    Thread.sleep(1000)
    log("New Thread running.")
    Thread.sleep(1000)
    log("still running")
    Thread.sleep(1000)
    log("Completed.")
  }
  t.join()
  log("New thread joined.")
}
