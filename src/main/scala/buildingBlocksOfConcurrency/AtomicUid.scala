package buildingBlocksOfConcurrency

import java.util.concurrent.atomic.AtomicLong

/**
  * Created by christian on 17/05/17.
  */
object AtomicUid extends App{
  private val uid = new AtomicLong(0L)
  def getUniqueId(): Long = uid.incrementAndGet()
  execute { log(s"Uid asynchronously: ${getUniqueId()}")}
  log(s"Got a unique id: ${getUniqueId()}")
}
