package jvmConcurrency.sandbox

/**
  * Created by clemus on 5/16/2017.
  */
object ResolvedDeadlock extends App{

  import ThreadsProtectedUid.getUniqueId

  class Account (val name: String, var money: Int) {
    val uid = getUniqueId()
  }

  def send(a: Account, b: Account, n: Int) = {
    def adjust() = {
      a.money -= n
      b.money += n
    }
    if(a.uid < b.uid)
      a.synchronized { b.synchronized { adjust() } }
    else
      b.synchronized { a.synchronized { adjust() } }
  }


  val a = new Account("Jack", 1000)
  val b = new Account("Jill", 2000)
  val t1 = thread { for(i<- 0 until 100) send (a,b,1)}
  val t2 = thread { for(i<- 0 until 100) send (b,a,1)}
  t1.join();t2.join()
  log(s"a = ${a.money}, b = ${b.money}")
}
