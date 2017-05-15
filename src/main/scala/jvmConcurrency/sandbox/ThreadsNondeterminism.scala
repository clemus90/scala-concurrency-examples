package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/15/2017.
  */
object ThreadsNondeterminism extends App{
  val t = thread{ log("New thread running.")}
  log("...")
  log("...")
  t.join()
  log("New thread joined.")
}
