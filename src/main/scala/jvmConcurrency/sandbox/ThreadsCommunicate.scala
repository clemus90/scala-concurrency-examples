package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/15/2017.
  */
object ThreadsCommunicate extends App{
  var result:String = null
  val t = thread{ result = "\nTitle\n" + "=" * 5}
  t.join()
  log(result)
}
