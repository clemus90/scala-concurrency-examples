package buildingBlocksConcurrency

import scala.concurrent._
/**
  * Created by clemus on 5/17/2017.
  */
object ExcecutionContextCreate extends App {
  val pool = new forkjoin.ForkJoinPool(2)
  val ectx = ExecutionContext.fromExecutorService(pool)
  ectx.execute(new Runnable {
    override def run(): Unit = log("Running on the execution context again.")
  })

  Thread.sleep(500)
}
