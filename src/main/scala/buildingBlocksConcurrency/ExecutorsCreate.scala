package buildingBlocksConcurrency

import java.util.concurrent.ForkJoinPool

/**
  * Created by clemus on 5/17/2017.
  */
object ExecutorsCreate extends App{
  val executor = new ForkJoinPool
  executor.execute(new Runnable {
    override def run(): Unit = log("This task is run asynchronously.")
  })
  import java.util.concurrent.TimeUnit
  executor.shutdown()
  executor.awaitTermination(60, TimeUnit.SECONDS)
}
