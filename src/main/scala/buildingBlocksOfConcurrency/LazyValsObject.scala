package buildingBlocksOfConcurrency

/**
  * Created by christian on 17/05/17.
  */
object LazyValsObject extends App {
  object Lazy { log("Running Lazy constructor.") }
  log("Main thread is about to reference Lazy.")
  Lazy
  log("Main thread completed.")
}
