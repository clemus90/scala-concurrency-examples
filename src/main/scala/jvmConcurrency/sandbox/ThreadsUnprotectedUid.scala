package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/15/2017.
  */
object ThreadsUnprotectedUid extends App {
  var uidCount = 0L

  def getUniqueId() = {
    val freshUid = uidCount + 1
    uidCount = freshUid
    freshUid
  }

  def printUniqueIds(n: Int):Unit = {
    val uids = for (i<- 0 until n) yield getUniqueId()
    log(s"Generated uids: $uids")
  }

  val t = thread { printUniqueIds(5) }
  printUniqueIds(5)
  t.join()

}
