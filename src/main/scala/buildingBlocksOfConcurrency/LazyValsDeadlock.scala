package buildingBlocksOfConcurrency

/**
  * Created by christian on 17/05/17.
  */
object LazyValsDeadlock extends App {
  object A { lazy val x: Int = B.y }
  object B { lazy val y: Int = A.x }
  execute { B.y }
  A.x
}
