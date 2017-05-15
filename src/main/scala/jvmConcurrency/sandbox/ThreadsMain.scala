package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/15/2017.
  */
object ThreadsMain extends App{
  val t: Thread = Thread.currentThread()
  val name = t.getName
  println(s"I am the thread $name")
}
