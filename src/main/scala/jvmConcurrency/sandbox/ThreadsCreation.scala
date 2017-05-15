package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/15/2017.
  */
object ThreadsCreation extends App{
  class MyThread extends Thread{
    override def run(): Unit = {
      println("New thread running.")
    }
  }

  val t = new MyThread
  t.start()
  t.join()
  println("New Thread joined.")
}
