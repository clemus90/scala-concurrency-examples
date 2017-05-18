package buildingBlocksConcurrency
import scala.concurrent._
/**
  * Created by clemus on 5/17/2017.
  */
object ExecutionsContextGlobal extends App{
  val ectx = ExecutionContext.global
  ectx.execute(new Runnable {
    override def run(): Unit = log("Running on the execution context.")
  })
  Thread.sleep(500)
}
