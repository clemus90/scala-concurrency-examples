package buildingBlocksOfConcurrency

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

/**
  * Created by christian on 17/05/17.
  */
object FilesystemExample extends App{

  sealed trait State
  class Idle extends State
  class Creating extends State
  class Copying(val n:Int) extends State
  class Deleting extends State

  class Entry(val isDir: Boolean) {
    val state = new AtomicReference[State](new Idle)

  }

  @tailrec private def prepareForDelete(entry: Entry): Boolean = {
    val s0 = entry.state.get
    s0 match {
      case i: Idle =>
        if(entry.state.compareAndSet(s0, new Deleting)) true
        else prepareForDelete(entry)
      case c: Creating =>
        false
        //logMessage("File currently created, cannot delete."); false
      case c: Copying =>
        false
        //logMessage("File currently copied, cannot delete."); false
      case d: Deleting =>
        false
    }
  }


}

