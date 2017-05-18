/**
  * Created by christian on 18/05/17.
  */
package object futuresAndPromises {
  def log(msg: String): Unit = {
    println(s"${Thread.currentThread.getName}> $msg")
  }
}
