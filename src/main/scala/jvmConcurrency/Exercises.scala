package jvmConcurrency

import scala.collection.mutable


/**
  * Created by christian on 16/05/17.
  */
object Exercises extends App{
  /**
    * 1) parallel, using two threads
    */
  def parallel[A, B](a: =>A, b: =>B): (A,B) = {
    var retA: Option[A] = None
    var retB: Option[B] = None
    val t1 = thread {retA = Some(a)}
    val t2 = thread {retB = Some(b)}
    t1.join()
    t2.join()
    (for(x <- retA; y <- retB) yield (x,y)) match {
      case Some((x0,y0)) => (x0,y0)
      case None => throw new Exception("Computation not finished successfully")
    }
  }

  /**
    * 2) Periodic execution of a computation
    */

  def periodically(duration: Long)(b: => Unit): Unit = {
    thread {b}
    Thread.sleep(duration)
    periodically(duration)(b)
  }

  /**
    * 3) SyncVar to share data between threads
    */

  class SyncVar[T] {
    var v: Option[T] = None
    def get(): T = v.synchronized {
      val ret = v match {
        case Some(x) => x
        case None => throw new Exception("Get over empty var")
      }
      v = None
      ret
    }

    def put(x: T): Unit = v.synchronized {
      v match {
        case Some(x) => throw new Exception("There is already a value stored")
        case None => v = Some(x)
      }
    }

    /**
      * 4) Check methods, is Empty and nonEmpty
      */
    def isEmpty(): Boolean = v.synchronized{
      return v.isEmpty
    }

    def nonEmpty(): Boolean = !isEmpty()

    /**
      * 5) avoiding busy waiting, or handling of guard locks from the consumers
      */
    def getWait(): T = this.synchronized{
      while (isEmpty()) wait()
      val ret = get()
      notify()
      ret
    }

    def putWait(x: T): Unit = this.synchronized{
      while (nonEmpty()) wait()
      put(x)
      notify()
    }
  }

  /**
    * 4 cont) producer and consumer of vals in the SyncVar
    */

  val sv1 = new SyncVar[Int]()
  val consumer = thread {
    sv1.synchronized {
      var finished = false
      while(!finished) {
        while (sv1.isEmpty()) sv1.wait()
        val msg = sv1.get()
        sv1.notify()
        println(s"VALUE RECEIVED: $msg")
        finished = msg == -1
      }
    }
  }
  val producer = thread {
    sv1.synchronized {
      for (x <- 0 until 15) {
        while (sv1.nonEmpty()) sv1.wait()
        sv1.put(x)
        sv1.notify()
      }
      while (sv1.nonEmpty()) sv1.wait()
      sv1.put(-1)
      sv1.notify()
    }
  }

  consumer.join()
  producer.join()

  /**
    * 5 cont) reimplement the consumer and producer to use the waiting approach
    */
  println("5) Now using the wait methods ...")

  val consumer1 = thread {
    var finished = false
    while(!finished){
      val msg = sv1.getWait()
      println(s"VALUE RECEIVED: $msg")
      finished = msg == -1
    }
  }

  val producer1 = thread {
    for(x <- 0 until 15) sv1.putWait(x)
    sv1.putWait(-1)
  }

  consumer1.join()
  producer1.join()

  /**
    * 6) SyncQueue, n values of type T
    */
  import scala.collection.mutable
  class SyncQueue[T](n: Int) {
    var q: mutable.Queue[T] = mutable.Queue()
    def get(): T = q.synchronized {
      val ret = q.isEmpty match {
        case false => q.dequeue()
        case true => throw new Exception("Get over empty queue")
      }
      ret
    }

    def put(x: T): Unit = q.synchronized {
      if (q.size < n) q.enqueue(x)
      else throw new Exception("The queue is at full capacity")
    }

    /**
      * Check methods, is Empty and nonEmpty
      */
    def isEmpty(): Boolean = q.synchronized{
      return q.isEmpty
    }

    def nonEmpty(): Boolean = !isEmpty()

    def isFull(): Boolean = q.size == n

    /**
      * avoiding busy waiting, or handling of guard locks from the consumers
      */
    def getWait(): T = this.synchronized{
      while (isEmpty()) wait()
      val ret = get()
      notify()
      ret
    }

    def putWait(x: T): Unit = this.synchronized{
      while (isFull()) wait()
      put(x)
      notify()
    }
  }

  //Testing new class

  println("6 cont) Testing queue ...")

  val sq: SyncQueue[Int] = new SyncQueue[Int](2)

  val consumer2 = thread {
    var finished = false
    while(!finished){
      val msg = sq.getWait()
      println(s"VALUE RECEIVED: $msg")
      finished = msg == -1
    }
  }

  val producer2 = thread {
    for(x <- 0 until 15) sq.putWait(x)
    sq.putWait(-1)
  }

  consumer2.join()
  producer2.join()

  /**
    * 7) Send all to money from n accounts to an account a0
    */
  import jvmConcurrency.sandbox.ResolvedDeadlock.Account
  import jvmConcurrency.sandbox.ResolvedDeadlock.send

  def sendAll(accounts: Set[Account], target: Account) = {
    for(sender <- accounts){
      send(sender, target, sender.money)
    }
  }

  /**
    * 8) Creating a Worker with priority
    */
  class PriorityTaskPool(n: Int){
    private val tasks = mutable.PriorityQueue[(Int, () => Unit)]()(Ordering.by(_._1))

    class Worker extends Thread {
      var terminated = false

      def poll(): Option[() => Unit] = tasks.synchronized{
        while(tasks.isEmpty && !terminated) tasks.wait()
        if (!terminated) Some(tasks.dequeue()._2) else None
      }

      override def run():Unit = poll() match {
        case Some(v) =>  v(); run()
        case None =>
      }

      def shutdown() = tasks.synchronized{
        terminated = true
        tasks.notify()
      }
    }
    val workers = for(i <- 1 to n) yield new Worker()

    for(w <- workers) w.start()

    def asynchronous(priority: Int)(body: =>Unit) = tasks.synchronized{
      tasks.enqueue((priority, () => body))
      tasks.notify()
    }

    def shutdown() = for(w<- workers)w.shutdown()
  }

  println("Testing exercise 8, 9 and 10")
  val pool = new PriorityTaskPool(3)
  pool.asynchronous(2) {log("Hello"); Thread.sleep(200)}
  for(_ <- 0 until 10) pool.asynchronous(2) {log("world!"); Thread.sleep(200)}
  for(_ <- 0 until 2) pool.asynchronous(3) {log(" important!!!!"); ; Thread.sleep(200)}
  Thread.sleep(500)
  pool.shutdown()


  class ConcurrentBiMap[K, V] {
    var mapStraight: mutable.Map[K,V] = mutable.Map()
    var mapReverse: mutable.Map[V,K] = mutable.Map()

    def reserve[T](t: => T): T = mapStraight.synchronized{
      mapReverse.synchronized{
        t
      }
    }

    def put(k:K, v:V): Option[(K, V)] = reserve{
        val res1 = mapStraight.put(k,v)
        val res2 = mapReverse.put(v,k)
        for(r1 <- res1; r2 <- res2) yield (r2, r1)
    }

    def removeKey(k:K): Option[V]= reserve{
      val res1 = mapStraight.remove(k)
      res1 match {
        case Some(r1) =>
          val res2 = mapReverse.remove(r1)
          res2 match {
            case Some(_) => res1
            case None => None
          }
        case None => None
      }
    }
    def removeValue(v:V): Option[K]= reserve{
      val res1 = mapReverse.remove(v)
      res1 match {
        case Some(r1) =>
          val res2 = mapStraight.remove(r1)
          res2 match {
            case Some(_) => res1
            case None => None
          }
        case None => None
      }
    }
    def getValue(k:K): Option[V]= ???
    def getKey(v:V): Option[K]= ???
    def size: Int = ???
    def iterator: Iterator[(K, V)]= ???
  }
}
